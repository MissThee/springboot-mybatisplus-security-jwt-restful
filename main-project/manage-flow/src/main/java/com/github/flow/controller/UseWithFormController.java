package com.github.flow.controller;

import com.github.common.config.exception.custom.MyMethodArgumentNotValidException;
import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.tool.Res;
import com.github.flow.dto.FormDataDTO;
import com.github.flow.dto.ProcessDefinitionDTO;
import com.github.flow.dto.ProcessInstanceDTO;
import com.github.flow.dto.TaskDTO;
import com.github.flow.vo.ManaVO;
import com.github.flow.vo.UseVO;
import com.github.flow.vo.UseWithFormVO;
import com.google.common.base.Joiner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.form.*;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.impl.form.DateFormType;
import org.flowable.engine.impl.form.EnumFormType;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "工作流-审批流程流转(附带表单)")
@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping("flowable")
public class UseWithFormController {
    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final FormService formService;
    private final MapperFacade mapperFacade;

    @Autowired
    public UseWithFormController(@Qualifier("processEngine") ProcessEngine processEngine, MapperFacade mapperFacade) {
        this.runtimeService = processEngine.getRuntimeService();
        this.taskService = processEngine.getTaskService();
        this.repositoryService = processEngine.getRepositoryService();
        this.formService = processEngine.getFormService();
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "流程定义信息-查询多个，所有定义最新版", notes = "")
    @PostMapping("processdefinition")
    public Res<ManaVO.SearchNewestProcessDefinitionRes> searchNewestProcessDefinition() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        List<ProcessDefinitionDTO> processDefList = list
                .stream()
                .map(e -> {
                    ProcessDefinitionDTO processDefinitionDTO = mapperFacade.map(e, ProcessDefinitionDTO.class);
                    processDefinitionDTO.setIsSuspended(e.isSuspended());
                    return processDefinitionDTO;
                })
                .sorted((a, b) -> {
                    //将未挂起的流程定义排在前面
                    if (a.getIsSuspended() && !b.getIsSuspended()) {
                        return 1;
                    } else if (!a.getIsSuspended() && !b.getIsSuspended()) {
                        return -1;
                    } else {
                        return 0;
                    }
                })
                .collect(Collectors.toList());
        return Res.success(new ManaVO.SearchNewestProcessDefinitionRes().setProcessDefinitionList(processDefList));
    }

    // act_ru_task          启动流程中的任务，仅存放正在执行的任务，执行完的任务不在本表。
    // act_ru_identitylink  存放正在执行任务的，办理人信息
    @ApiOperation(value = "任务-查询多个，待执行", notes = "（按办理人查询，按候选办理人查询，按候选办理组查询）")
    @PostMapping("task")
    public Res<UseVO.SearchTaskRes> searchTask(@RequestBody @Validated UseVO.SearchTaskReq req) {

        TaskQuery taskQuery = taskService.createTaskQuery();
        if (req.getAssignee() != null) {
            taskQuery.taskAssignee(req.getAssignee());//按办理人查询
        }
        if (req.getCandidateUser() != null) {
            taskQuery.taskCandidateOrAssigned(req.getCandidateUser());//按候选办理人查询。
//            taskQuery.taskCandidateUser(candidateUser);//按候选办理人查询。仅无办理人，且有候选人的任务可查到
        }
        if (req.getCandidateGroup() != null && req.getCandidateGroup().size() > 0) {
            taskQuery.taskCandidateGroupIn(req.getCandidateGroup());//按候选组查询。
        }
        if (req.getIsOnlyUnassigned()) {
            taskQuery.taskUnassigned();
        }
        taskQuery
                .orderByProcessInstanceId().asc()
                .orderByTaskCreateTime().desc();
        long total = taskQuery.count();
        List<Task> list = taskQuery.listPage(req.getPageIndex() * req.getPageSize(), (req.getPageIndex() + 1) * req.getPageSize());
        List<TaskDTO> taskList = list.stream().map(e -> {
            TaskDTO taskDTO = mapperFacade.map(e, TaskDTO.class);
            taskDTO.setIsSuspended(e.isSuspended());
            return taskDTO;
        }).collect(Collectors.toList());
        UseVO.SearchTaskRes searchTaskRes = new UseVO.SearchTaskRes()
                .setTaskList(taskList)
                .setTotal(total);
        return Res.success(searchTaskRes);
    }

    @ApiOperation(value = "表单-查询开始节点表单属性")
    @PostMapping("form/start")
    public Res<UseWithFormVO.GetStartFormDataRes> getStartFormData(@RequestBody UseWithFormVO.GetStartFormDataReq req) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        String processDefinitionId;
        if (!StringUtils.isEmpty(req.getProcessDefinitionId())) {
            processDefinitionId = req.getProcessDefinitionId();
        } else if (!StringUtils.isEmpty(req.getProcessDefinitionKey())) {
            processDefinitionId = processDefinitionQuery.processDefinitionKey(req.getProcessDefinitionKey()).latestVersion().singleResult().getId();
        } else {
            throw new MissingFormatArgumentException("缺少查询条件。需要processDefinitionId或processDefinitionKey");
        }

        StartFormData startFormData = formService.getStartFormData(processDefinitionId);
        UseWithFormVO.GetStartFormDataRes getStartFormDataRes = new UseWithFormVO.GetStartFormDataRes()
                .setFormProperty(buildFormDataMap(startFormData));
        return Res.success(getStartFormDataRes);
    }

    @ApiOperation(value = "表单-任务-开始流程，并添加表单的内容")
    @PutMapping("form/start")
    public Res startForm(@RequestBody UseWithFormVO.StartFormReq req) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (!StringUtils.isEmpty(req.getProcessDefinitionId())) {
            processDefinitionQuery.processDefinitionId(req.getProcessDefinitionId());
        } else if (!StringUtils.isEmpty(req.getProcessDefinitionKey())) {
            processDefinitionQuery.processDefinitionKey(req.getProcessDefinitionKey());
        } else {
            throw new MissingFormatArgumentException("缺少查询条件。需要processDefinitionId或processDefinitionKey");
        }
        ProcessDefinition processDefinition = processDefinitionQuery.latestVersion().singleResult();
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), req.getBusinessKey(), req.getVariableMap());
        UseWithFormVO.StartFormRes startFormRes = new UseWithFormVO.StartFormRes().setProcessInstance(mapperFacade.map(processInstance, ProcessInstanceDTO.class));
        return Res.success(startFormRes, "创建成功");
    }

    @ApiOperation(value = "表单-任务-查询节点表单属性，及多出口时的判断值")
    @PostMapping("form/task")
    public Res<UseWithFormVO.GetTaskFormDataRes> getTaskFormData(@RequestBody @Validated UseWithFormVO.GetTaskFormDataReq req) {
        // 收集本节点出口可选值
        Set<String> nextOutValueSet = new HashSet<>();
        {
            Task task = taskService.createTaskQuery().taskId(req.getTaskId()).singleResult();
            if (task != null) {
                ExecutionEntity ee = (ExecutionEntity) runtimeService.createExecutionQuery()
                        .executionId(task.getExecutionId()).singleResult();
                // 当前审批节点
                String currentActivityId = ee.getActivityId();
                BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(currentActivityId);
                //获取节点输出集合
                List<SequenceFlow> outgoingFlowList = flowNode.getOutgoingFlows();
                // 输出连线（规定图中下一节点为网关时，本节点只有一个输出；直接分支可为多条输出。）
                if (outgoingFlowList != null) {
                    if (outgoingFlowList.size() > 1) {
                        for (SequenceFlow sequenceFlow : outgoingFlowList) {
                            nextOutValueSet.add(sequenceFlow.getName());
                        }
                    } else if (outgoingFlowList.size() == 1) {
                        //获取第一条输出线
                        SequenceFlow sequenceFlow = outgoingFlowList.get(0);
                        //获取第一条输出线末端节点
                        FlowElement flowElement = sequenceFlow.getTargetFlowElement();
                        if (flowElement instanceof ExclusiveGateway) {
                            List<SequenceFlow> subOutgoingFlowList = ((ExclusiveGateway) flowElement).getOutgoingFlows();
                            for (SequenceFlow sequenceFlow1 : subOutgoingFlowList) {
                                nextOutValueSet.add(sequenceFlow1.getName());
                            }
                        }
                    }
                }
            }
        }
        TaskFormData taskFormData;
        try {
            taskFormData = formService.getTaskFormData(req.getTaskId());
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failure("查询失败，无此任务");
        }
        UseWithFormVO.GetTaskFormDataRes getTaskFormDataRes = new UseWithFormVO.GetTaskFormDataRes()
                .setFormProperty(buildFormDataMap(taskFormData))
                .setNextOutValueList(nextOutValueSet);
        return Res.success(getTaskFormDataRes);
    }

    @ApiOperation(value = "表单-任务-保存属性并完成任务")
    @PutMapping("form/task")
    @Transactional(rollbackFor = Exception.class)
    public Res submitTaskFormData(HttpServletRequest httpServletRequest, @RequestBody @Validated UseWithFormVO.SubmitTaskFormDataReq req) throws MyMethodArgumentNotValidException {
        taskService.setAssignee(req.getTaskId(), JavaJWT.getId(httpServletRequest));
        if (!StringUtils.isEmpty(req.getComment())) {
            Authentication.setAuthenticatedUserId(JavaJWT.getId(httpServletRequest));//批注人为线程绑定变量，需在同一线程内设置批注人信息。setAuthenticatedUserId的实际实现类中，使用的ThreadLocal保存变量
            String processInstanceId = taskService.createTaskQuery()
                    .taskId(req.getTaskId())
                    .singleResult()
                    .getProcessInstanceId();
            taskService.addComment(req.getTaskId(), processInstanceId, req.getComment());
        }
        if (req.getVariableMap() != null) {
            taskService.setVariables(req.getTaskId(), req.getVariableMap());
        }
        if (req.getFormVariableMap() != null) {
            {//判断提交内容是否有当前任务表单之外的字段，有则抛出异常，回滚事务。
                TaskFormData taskFormData = formService.getTaskFormData(req.getTaskId());
                Map<String, FormDataDTO> stringFormDataDTOMap = buildFormDataMap(taskFormData);
                Set<String> keySet = stringFormDataDTOMap.keySet();
                Map<String, String> formVariableMap = req.getFormVariableMap();
                List<String> invalidVariableList = new ArrayList<>();
                for (String key : formVariableMap.keySet()) {
                    if (!keySet.contains(key)) {
                        invalidVariableList.add(key);
                    }
                }
                if (invalidVariableList.size() > 0) {
                    throw new MyMethodArgumentNotValidException("表单无以下字段，不可提交这些值：" + Joiner.on(",").join(invalidVariableList));
                }
            }
            //        Map<String, String> filteredFormVariableMap = formVariableMap.entrySet().stream().filter(e -> keySet.contains(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            formService.saveFormData(req.getTaskId(), req.getFormVariableMap());
        }
        try {
            taskService.complete(req.getTaskId());
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failure("操作失败，请稍后重试");
        }
        return Res.success("操作成功");
    }

    /**
     * 构建表单属性list集合
     *
     * @param taskFormData
     * @return 表单属性list集合
     */
    private List<FormDataDTO> buildFormDataList(FormData taskFormData) {//返回为集合类型
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        List<FormDataDTO> list = new ArrayList<>();
        for (FormProperty formProperty : formProperties) {
            if (formProperty.isReadable()) {
                FormDataDTO myFormProperty = buildFormDataDTO(formProperty);
                list.add(myFormProperty);
            }
        }
        return list;
    }

    /**
     * 构建表单属性map集合
     *
     * @param taskFormData
     * @return 表单属性map集合
     */
    private Map<String, FormDataDTO> buildFormDataMap(FormData taskFormData) {//返回为对象类型，key为字段id
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        Map<String, FormDataDTO> map = new HashMap<>();
        for (FormProperty formProperty : formProperties) {
            if (formProperty.isReadable()) {
                FormDataDTO myFormProperty = buildFormDataDTO(formProperty);
                map.put(myFormProperty.getId(), myFormProperty);
            }
        }
        return map;
    }

    /**
     * 表单中单个变量值的信息转为DTO
     *
     * @param formProperty
     * @return 表单字段DTO
     */
    private FormDataDTO buildFormDataDTO(FormProperty formProperty) {
        FormDataDTO myFormProperty = mapperFacade.map(formProperty, FormDataDTO.class);
        myFormProperty.setIsRequired(formProperty.isRequired());
        myFormProperty.setIsWritable(formProperty.isWritable());
        FormType formType = formProperty.getType();
        myFormProperty.setType(formType.getName());//类型值可能为：date enum double boolean double long string
        //"datePattern"、"values"此值在每个类型中是固定的，于源码中查看。默认类型中仅datePattern、values有getInformation方法
        if (formType instanceof DateFormType) {
            myFormProperty.setInformation(formProperty.getType().getInformation("datePattern"));
        } else if (formType instanceof EnumFormType) {
            myFormProperty.setInformation(formProperty.getType().getInformation("values"));
        }
        return myFormProperty;
    }
}




