package com.github.flow.controller;

import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.tool.Res;
import com.github.flow.common.FUtils;
import com.github.flow.dto.FormDataDTO;
import com.github.flow.dto.ProcessInstanceDTO;
import com.github.flow.vo.UseWithFormVO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Api(tags = "工作流-审批流程流转(附带表单)")
@RestController
@RequestMapping("flowable/form")
public class UseWithFormController {
    private final FUtils fUtils;
    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final FormService formService;
    private final MapperFacade mapperFacade;

    @Autowired
    public UseWithFormController(@Qualifier("processEngine") ProcessEngine processEngine, FUtils fUtils, MapperFacade mapperFacade) {
        this.runtimeService = processEngine.getRuntimeService();
        this.taskService = processEngine.getTaskService();
        this.repositoryService = processEngine.getRepositoryService();
        this.formService = processEngine.getFormService();
        this.fUtils = fUtils;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "表单-查询开始节点表单属性")
    @PostMapping("start")
    public Res<UseWithFormVO.GetStartFormDataRes> getStartFormData(@RequestBody UseWithFormVO.GetStartFormDataReq req) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        String processDefinitionId = null;
        if (!StringUtils.isEmpty(req.getProcessDefinitionId())) {
            processDefinitionId = req.getProcessDefinitionId();
        } else if (!StringUtils.isEmpty(req.getProcessDefinitionKey())) {
            processDefinitionId = processDefinitionQuery.processDefinitionKey(req.getProcessDefinitionKey()).latestVersion().singleResult().getId();
        }

        if (StringUtils.isEmpty(processDefinitionId)) {
            throw new MissingFormatArgumentException("缺少查询条件。需要processDefinitionId或processDefinitionKey");
        }
        StartFormData startFormData = formService.getStartFormData(processDefinitionId);
        UseWithFormVO.GetStartFormDataRes getStartFormDataRes = new UseWithFormVO.GetStartFormDataRes()
                .setFormProperty(buildFormDataMap(startFormData));
        return Res.success(getStartFormDataRes);
    }

    @ApiOperation(value = "表单-任务-开始流程，并添加表单的内容")
    @PutMapping("start")
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

    @ApiOperation(value = "表单-任务-查询节点表单属性")
    @PostMapping
    public Res<UseWithFormVO.GetTaskFormDataRes> getTaskFormData(@RequestBody @Validated UseWithFormVO.GetTaskFormDataReq req) {
        // 收集本节点出口可选值
        List<String> nextOutValueList = new ArrayList<>();
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
                List<SequenceFlow> outFlows = flowNode.getOutgoingFlows();
                // 输出连线（规定图中下一节点为网关时，本节点只有一个输出；直接分支可为多条输出。）
                if (outFlows != null) {
                    if (outFlows.size() > 1) {
                        for (SequenceFlow sequenceFlow : outFlows) {
                            nextOutValueList.add(sequenceFlow.getName());
                        }
                    } else if (outFlows.size() == 1) {
                        //获取第一条输出线
                        SequenceFlow sequenceFlow = outFlows.get(0);
                        //获取第一条输出线末端节点
                        FlowElement flowElement = sequenceFlow.getTargetFlowElement();
                        if (flowElement instanceof ExclusiveGateway) {
                            List<SequenceFlow> outgoingFlowList = ((ExclusiveGateway) flowElement).getOutgoingFlows();
                            for (SequenceFlow sequenceFlow1 : outgoingFlowList) {
                                nextOutValueList.add(sequenceFlow1.getName());
                            }
                        }
                    }
                }
            }
        }
        TaskFormData taskFormData = formService.getTaskFormData(req.getTaskId());
        UseWithFormVO.GetTaskFormDataRes getTaskFormDataRes = new UseWithFormVO.GetTaskFormDataRes()
                .setFormProperty(buildFormDataMap(taskFormData))
                .setNextOutValueList(nextOutValueList);
        return Res.success(getTaskFormDataRes);
    }

    @ApiOperation(value = "表单-任务-保存属性并完成任务")
    @PutMapping
    public Res submitTaskFormData(HttpServletRequest httpServletRequest, @RequestBody @Validated UseWithFormVO.SubmitTaskFormDataReq req) {
        taskService.setAssignee(req.getTaskId(), JavaJWT.getId(httpServletRequest));
        if (!StringUtils.isEmpty(req.getComment())) {
            Authentication.setAuthenticatedUserId(JavaJWT.getId(httpServletRequest));//批注人为线程绑定变量，需在同一线程内设置批注人信息。setAuthenticatedUserId的实际实现类中，使用的ThreadLocal保存变量
            String processInstanceId = taskService.createTaskQuery()
                    .taskId(req.getTaskId())
                    .singleResult()
                    .getProcessInstanceId();
            taskService.addComment(req.getTaskId(), processInstanceId, req.getComment());
        }
        taskService.setVariables(req.getTaskId(), req.getVariableMap());
        formService.submitTaskFormData(req.getTaskId(), req.getFormVariableMap());
        return Res.success("操作成功");
    }

    private List<FormDataDTO> buildFormDataList(FormData taskFormData) {
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

    private Map<String, FormDataDTO> buildFormDataMap(FormData taskFormData) {
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




