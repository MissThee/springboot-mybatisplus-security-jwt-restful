package com.github.flow.controller;

import com.github.common.config.exception.custom.MyMethodArgumentNotValidException;
import com.github.common.tool.Res;
import com.github.flow.common.FUtils;
import com.github.flow.dto.FormDataDTO;
import com.github.flow.dto.ProcessInstanceDTO;
import com.github.flow.vo.UseWithFormVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.flowable.engine.*;
import org.flowable.engine.form.*;
import org.flowable.engine.impl.form.DateFormType;
import org.flowable.engine.impl.form.EnumFormType;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "表单-查询开始节点的表单属性")
    @PostMapping("start")
    public Res<UseWithFormVO.GetStartFormDataRes> getStartFormData(@RequestBody UseWithFormVO.GetStartFormDataReq req) throws MyMethodArgumentNotValidException {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        String processDefinitionId = null;
        if (!StringUtils.isEmpty(req.getProcessDefinitionId())) {
            processDefinitionId = req.getProcessDefinitionId();
        } else if (!StringUtils.isEmpty(req.getProcessDefinitionKey())) {
            processDefinitionId = processDefinitionQuery.processDefinitionKey(req.getProcessDefinitionKey()).singleResult().getId();
        }
        if (StringUtils.isEmpty(processDefinitionId)) {
            throw new MyMethodArgumentNotValidException("缺少查询条件processDefinitionId或processDefinitionKey");
        }
        StartFormData startFormData = formService.getStartFormData(processDefinitionId);
        UseWithFormVO.GetStartFormDataRes getStartFormDataRes = new UseWithFormVO.GetStartFormDataRes()
                .setFormProperty(formDataFormat(startFormData));
        return Res.success(getStartFormDataRes);
    }

    @ApiOperation(value = "表单-开始流程，并添加表单的内容")
    @PutMapping("start")
    public Res startForm(@RequestBody UseWithFormVO.StartFormReq req) throws MyMethodArgumentNotValidException {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (!StringUtils.isEmpty(req.getProcessDefinitionId())) {
            processDefinitionQuery.processDefinitionId(req.getProcessDefinitionId());
        } else if (!StringUtils.isEmpty(req.getProcessDefinitionKey())) {
            processDefinitionQuery.processDefinitionKey(req.getProcessDefinitionKey());
        } else {
            throw new MyMethodArgumentNotValidException("需要processDefinitionId或processDefinitionKey");
        }
        ProcessDefinition processDefinition = processDefinitionQuery.latestVersion().singleResult();
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), req.getVariableMap());
        UseWithFormVO.StartFormRes startFormRes = new UseWithFormVO.StartFormRes().setProcessInstance(mapperFacade.map(processInstance, ProcessInstanceDTO.class));
        return Res.success(startFormRes, "启动成功");
    }

    @ApiOperation(value = "表单-查询属性")
    @PostMapping
    public Res<UseWithFormVO.GetTaskFormDataRes> getTaskFormData(@RequestBody @Validated UseWithFormVO.GetTaskFormDataReq req) {
        TaskFormData taskFormData = formService.getTaskFormData(req.getTaskId());
        UseWithFormVO.GetTaskFormDataRes getTaskFormDataRes = new UseWithFormVO.GetTaskFormDataRes()
                .setFormProperty(formDataFormat(taskFormData))
                .setFormKey(taskFormData.getFormKey());
        return Res.success(getTaskFormDataRes);
    }

    @ApiOperation(value = "表单-保存属性")
    @PatchMapping
    public Res saveTaskFormData(@RequestBody @Validated UseWithFormVO.SaveTaskFormDataReq req) {
        formService.saveFormData(req.getTaskId(), req.getVariableMap());
        return Res.success("保存成功");
    }

    @ApiOperation(value = "表单-保存属性并完成任务")
    @PutMapping
    public Res submitTaskFormData(@RequestBody @Validated UseWithFormVO.SubmitTaskFormDataReq req) {
        formService.submitTaskFormData(req.getTaskId(), req.getVariableMap());
        return Res.success("操作成功");
    }

    private List<FormDataDTO> formDataFormat(FormData taskFormData) {
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        List<FormDataDTO> list = new ArrayList<>();
        for (FormProperty formProperty : formProperties) {
            if (formProperty.isReadable()) {
                FormDataDTO myFormProperty = mapperFacade.map(formProperty, FormDataDTO.class);
//                FormDataDTO myFormProperty=new FormDataDTO() ;
//                BeanUtils.copyProperties(formProperty,myFormProperty);
//                MyCopier.copy(formProperty,myFormProperty);
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
                list.add(myFormProperty);
            }
        }
        return list;
    }

//    //查询流程中开始节点的表单渲染
//    @PostMapping("render/start")
//    public Res getRenderedStartForm(@RequestBody(required = false) Map bJO) throws MyMethodArgumentNotValidException {
//        String taskId = (String) bJO.getOrDefault("taskId", null);
//        String processInstanceId = (String) bJO.getOrDefault("processInstanceId", null);
//        if (taskId != null) {
//            processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
//        } else if (processInstanceId != null) {
//
//        } else {
//            throw new MyMethodArgumentNotValidException("需要taskId、processInstanceId");
//        }
//        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
//        Object renderedStartForm = formService.getRenderedStartForm(processInstance.getProcessDefinitionId());
//        return Res.success(renderedStartForm);
//    }
//
//    //查询流程中开始节点的表单渲染
//    @PostMapping("render")
//    public Res getRenderedForm(@RequestBody(required = false) Map bJO) throws MyMethodArgumentNotValidException {
//        String taskId = (String) bJO.getOrDefault("taskId", null);
//        if (taskId != null) {
//            return Res.success(formService.getRenderedTaskForm(taskId));
//        } else {
//            throw new MyMethodArgumentNotValidException("需要taskId");
//        }
//    }
}




