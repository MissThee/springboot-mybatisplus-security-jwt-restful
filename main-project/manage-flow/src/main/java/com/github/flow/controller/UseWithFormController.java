package com.github.flow.controller;

import com.github.common.tool.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.*;
import org.flowable.engine.form.*;
import org.flowable.engine.impl.form.DateFormType;
import org.flowable.engine.impl.form.EnumFormType;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.github.flow.controller.FJSON.*;

@Api(tags = "工作流-审批流程流转(带form变量约束)")
@RestController
@RequestMapping("flowable/form")
public class UseWithFormController {
    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final FormService formService;

    @Autowired
    public UseWithFormController(@Qualifier("processEngine") ProcessEngine processEngine) {
        this.runtimeService = processEngine.getRuntimeService();
        this.taskService = processEngine.getTaskService();
        this.repositoryService = processEngine.getRepositoryService();
        this.formService = processEngine.getFormService();
    }

    @ApiOperation(value = "查询流程开始节点的表单属性", notes = "通过taskId或executionId")
    @PostMapping("getStartFormData")
    public Res<Map> getStartFormData(@RequestBody(required = false) Map bJO) {
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
        Map jO = new HashMap();
        jO.put("部署id", startFormData.getDeploymentId());
        jO.put("formKey", startFormData.getFormKey());
        ProcessDefinition processDefinition = startFormData.getProcessDefinition();
        jO.put("processDefinition", processDefinition.toString());
        jO.put("formProperty", formDataFormat(startFormData));
        return Res.success(jO);
    }

    @ApiOperation(value = "开始一个流程，并添加表单的内容", notes = "")
    @PostMapping("startForm")
    public Res startForm(@RequestBody(required = false) Map bJO) {
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

    @ApiOperation(value = "查询流程中任务的表单属性", notes = "")
    @PostMapping("getTaskFormData")
    public Res<Map> getTaskFormData(@RequestBody(required = false) Map bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        if (taskId != null) {
            Res.failure("need taskId or processInstanceId");
        }
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        Map jO = new HashMap();
        jO.put("部署id", taskFormData.getDeploymentId());
        jO.put("formKey", taskFormData.getFormKey());
        jO.put("formProperty", formDataFormat(taskFormData));
        return Res.success(jO);
    }

    @ApiOperation(value = "保存表单值 ", notes = "")
    @PostMapping("saveTaskFormData")
    public Res saveTaskFormData(@RequestBody(required = false) Map bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        Map<String, String> variableMap = getMapOrDefaultFromJO(bJO, "variableMap", null);
        if (taskId != null) {
            Res.failure("need taskId or processInstanceId");
        }
        formService.saveFormData(taskId, variableMap);
        return Res.success();
    }

    @ApiOperation(value = "保存表单值，并完成任务", notes = "")
    @PostMapping("submitTaskFormData")
    public Res submitTaskFormData(@RequestBody(required = false) Map bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        Map<String, String> variableMap = getMapOrDefaultFromJO(bJO, "variableMap", null);
        if (taskId != null) {
            Res.failure("need taskId or processInstanceId");
        }
        formService.submitTaskFormData(taskId, variableMap);
        return Res.success();
    }


    private static ArrayList formDataFormat(FormData taskFormData) {
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        ArrayList<LinkedHashMap<String, Object>> arrayList = new ArrayList<>();
        for (FormProperty formProperty : formProperties) {
            if (formProperty.isReadable()) {
                arrayList.add(new LinkedHashMap<String, Object>() {{
                    put("id", formProperty.getId());
                    put("name", formProperty.getName());
                    put("value", formProperty.getValue());
                    put("isWritable", formProperty.isWritable());
                    put("isRequired", formProperty.isRequired());
                    put("type", formProperty.getType().getName());
                    FormType formType = formProperty.getType();
                    if (formType instanceof DateFormType) {// date enum double boolean double long string
                        //"datePattern"此值在每个类型中是固定的，于源码中查看。默认类型中仅datePattern、values有getInformation方法
                        put("getInformation", formProperty.getType().getInformation("datePattern"));
                    } else if (formType instanceof EnumFormType) {
                        put("getInformation", formProperty.getType().getInformation("values"));
                    }
                }});
            }
        }
        return arrayList;
    }
}




