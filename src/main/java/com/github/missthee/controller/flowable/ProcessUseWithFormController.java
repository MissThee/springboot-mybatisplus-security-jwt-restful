package com.github.missthee.controller.flowable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.missthee.tool.Res;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.FormType;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.form.DateFormType;
import org.flowable.engine.impl.form.EnumFormType;
import org.flowable.engine.repository.DiagramLayout;
import org.flowable.engine.repository.DiagramNode;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ExecutionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.flowable.variable.api.history.HistoricVariableInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.missthee.controller.flowable.FJSON.*;

@RestController
@RequestMapping("flowable/form")
public class ProcessUseWithFormController {
    //基础配置类
    //private final ProcessEngine processEngine;
    //流程部署、修改、删除服务。主要操作表：act_ge_bytearray,act_re_deployment,act_re_model,act_re_procdef
    private final RepositoryService repositoryService;
    //流程的运行。主要操作表：act_ru...
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    //查询历史记录。主要操作表：act_hi...
    private final HistoryService historyService;
    //页面表单服务（较少使用）
    private final FormService formService;
    //对工作流的用户管理的表操作。主要操作表：act_id...
    private final IdentityService identityService;
    //管理器
    private final ManagementService managementService;
    private final ProcessEngine processEngine;

    @Autowired
    public ProcessUseWithFormController(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, FormService formService, HistoryService historyService, IdentityService identityService, ManagementService managementService, @Qualifier("processEngine") ProcessEngine processEngine) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.formService = formService;
        this.historyService = historyService;
        this.identityService = identityService;
        this.managementService = managementService;
        this.processEngine = processEngine;
    }

    //查询流程开始节点的表单属性
    @RequestMapping("getStartFormData")
    public Res<JSONObject> getStartFormData(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String processInstanceId = getStringOrDefaultFromJO(bJO, "processInstanceId", null);
        if (taskId != null) {
            processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
        } else if (processInstanceId != null) {

        } else {
            Res.failure("need taskId or processInstanceId");
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        StartFormData startFormData = formService.getStartFormData(processInstance.getProcessDefinitionId());
        JSONObject jO = new JSONObject();
        jO.put("部署id", startFormData.getDeploymentId());
        jO.put("formKey", startFormData.getFormKey());
        ProcessDefinition processDefinition = startFormData.getProcessDefinition();
        jO.put("processDefinition", processDefinition.toString());
        List<FormProperty> formProperties = startFormData.getFormProperties();
        JSONArray jA = new JSONArray();
        for (FormProperty formProperty : formProperties) {
            jA.add(new JSONObject() {{
                put("id", formProperty.getId());
                put("name", formProperty.getName());
                put("value", formProperty.getValue());
                put("type", formProperty.getType());
                FormType formType = formProperty.getType();
                String getInformationKey = "";
                if (formType instanceof DateFormType) {
                    getInformationKey = "datePattern";
                } else if (formType instanceof EnumFormType) {
                    getInformationKey = "datePattern";
                }
                put("getInformation", formProperty.getType().getInformation(getInformationKey));
            }});
        }
        jO.put("formProperty", jA);
        return Res.success(jO);
    }

    //查询流程中开始节点的表单渲染
    @RequestMapping("getRenderedStartForm")
    public Res getRenderedStartForm(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String processInstanceId = getStringOrDefaultFromJO(bJO, "processInstanceId", null);
        if (taskId != null) {
            processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
        } else if (processInstanceId != null) {

        } else {
            Res.failure("need taskId or processInstanceId");
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Object renderedStartForm = formService.getRenderedStartForm(processInstance.getProcessDefinitionId());
        return Res.success(renderedStartForm);
    }

    //开始一个流程，并添加表单的内容
    @RequestMapping("startForm")
    public Res startForm(@RequestBody(required = false) JSONObject bJO) {
        String processDefKey = getStringOrDefaultFromJO(bJO, "processDefKey", null);
        Map<String, String> variableMap = getMapOrDefaultFromJO(bJO, "variableMap", new HashMap<String, String>() {{
            put("startDate", getFormatDate(LocalDateTime.now()));//重要！！：设置表单内容时，参数格式须严格按照设置的格式，否则参数值和类型会为空。即使设置了为必须值，也不会因为值为空报错，因为提供了此字段
            put("endDate", getFormatDate(LocalDateTime.now().plusDays(10)));
            put("reason", "没有理由");
        }});
        if (StringUtils.isEmpty(processDefKey)) {
            return Res.failure("empty processDefKey");
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefKey).latestVersion().singleResult();
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), variableMap);
        return Res.success(processInstanceToJSON(processInstance), "启动成功");
    }
    private String getFormatDate(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm").format(localDateTime);
    }

    //查询流程中任务的表单属性
    @RequestMapping("getTaskFormData")
    public Res<JSONObject> getTaskFormData(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        if (taskId != null) {
            Res.failure("need taskId or processInstanceId");
        }
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        JSONObject jO = new JSONObject();
        jO.put("部署id", taskFormData.getDeploymentId());
        jO.put("formKey", taskFormData.getFormKey());
        Task task = taskFormData.getTask();
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        JSONArray jA = new JSONArray();
        for (FormProperty formProperty : formProperties) {
            jA.add(new JSONObject() {{
                put("id", formProperty.getId());
                put("name", formProperty.getName());
                put("value", formProperty.getValue());
                put("type", formProperty.getType());
                FormType formType = formProperty.getType();
                String getInformationKey = "";
                if (formType instanceof DateFormType) {
                    getInformationKey = "datePattern";//此值在每个类型中是固定的，于源码中查看
                } else if (formType instanceof EnumFormType) {
                    getInformationKey = "values";
                }
                put("getInformation", formProperty.getType().getInformation(getInformationKey));
            }});
        }
        jO.put("formProperty", jA);
        return Res.success(jO);
    }


    //保存表单值，不完成任务
    @RequestMapping("saveTaskFormData")
    public Res saveTaskFormData(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        Map<String, String> variableMap = getMapOrDefaultFromJO(bJO, "variableMap", null);
        if (taskId != null) {
            Res.failure("need taskId or processInstanceId");
        }
        formService.saveFormData(taskId, variableMap);
        return Res.success();
    }

    //保存表单值，完成任务
    @RequestMapping("submitTaskFormData")
    public Res submitTaskFormData(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        Map<String, String> variableMap = getMapOrDefaultFromJO(bJO, "variableMap", null);
        if (taskId != null) {
            Res.failure("need taskId or processInstanceId");
        }
        formService.submitTaskFormData(taskId, variableMap);
        return Res.success();
    }
}




