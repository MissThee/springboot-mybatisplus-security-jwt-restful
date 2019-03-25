package com.github.missthee.controller.flowable;

import com.alibaba.fastjson.JSONObject;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.job.api.Job;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FJSON {
    protected static String getStringOrDefaultFromJO(JSONObject jO, String key, String defaultValue) {
        if (jO == null) {
            return defaultValue;
        } else {
            if (jO.containsKey(key)) {
                return jO.getString(key);
            } else {
                return defaultValue;
            }
        }
    }

    protected static <V> Map<String,V> getMapOrDefaultFromJO(JSONObject jO, String key, HashMap<String,V> map) {
        if (jO == null) {
            return map;
        } else {
            if (jO.containsKey(key)) {
                return (Map<String,V>)jO.getJSONObject(key);
            } else {
                return map;
            }
        }
    }

    protected static Boolean getBooleanOrDefaultFromJO(JSONObject jO, String key, Boolean defaultValue) {
        if (jO == null) {
            return defaultValue;
        } else {
            if (jO.containsKey(key)) {
                return jO.getBoolean(key);
            } else {
                return defaultValue;
            }
        }
    }

    protected static Map<String, Object> deploymentToJSON(Deployment deployment) {
        return new LinkedHashMap<String, Object>() {{
            put("部署ID", deployment.getId());
            put("部署名称", deployment.getName());
            put("部署KEY", deployment.getKey());
            put("部署时间", deployment.getDeploymentTime().toString());
        }};
    }

    protected static Map<String, Object> processDefinitionToJSON(ProcessDefinition processDefinition) {
        return new LinkedHashMap<String, Object>() {{
            put("部署ID", processDefinition.getId());
            put("部署名称", processDefinition.getName());
            put("部署KEY", processDefinition.getKey());
            put("部署版本", String.valueOf(processDefinition.getVersion()));
        }};
    }

    protected static Map<String, Object> processInstanceToJSON(ProcessInstance processInstance) {
        return new LinkedHashMap<String, Object>() {{
            put("流程实例ID", processInstance.getId());
            put("流程实例名称", processInstance.getName());
            put("流程实例对应业务id", processInstance.getBusinessKey());
            put("流程实例开始时间", processInstance.getStartTime());
        }};
    }


    protected static Map<String, Object> taskToJSON(Task task) {
        return new LinkedHashMap<String, Object>() {{
            put("任务ID", task.getId());
            put("任务名称", task.getName());
            put("任务创建时间", task.getCreateTime());
            put("任务办理人", task.getAssignee());
            put("执行实例ID", task.getExecutionId());
            put("流程实例ID", task.getProcessInstanceId());
            put("流程定义ID", task.getProcessDefinitionId());
        }};
    }

    protected static Map<String, Object> historicProcessToJSON(HistoricProcessInstance historicProcessInstance) {
        return new LinkedHashMap<String, Object>() {{
            put("历史流程实例ID", historicProcessInstance.getId());
            put("流程定义ID", historicProcessInstance.getProcessDefinitionId());
            put("流程定义KEY", historicProcessInstance.getProcessDefinitionKey());
            put("流程部署ID", historicProcessInstance.getDeploymentId());
            put("历史流程实例的业务id", historicProcessInstance.getBusinessKey());
            put("历史流程实例名称", historicProcessInstance.getName());//一般为空
            put("历史流程实例开始ID", historicProcessInstance.getStartActivityId());
            put("历史流程实例结束ID", historicProcessInstance.getEndActivityId());
            put("历史流程实例开始时间", historicProcessInstance.getStartTime());
            put("历史流程实例结束时间", historicProcessInstance.getEndTime());
        }};
    }

    protected static Map<String, Object> historyTaskToJSON(HistoricTaskInstance historicTaskInstance) {
        return new LinkedHashMap<String, Object>() {{
            put("历史任务ID", historicTaskInstance.getId());
            put("历史任务名称", historicTaskInstance.getName());
            put("历史任务创建时间", historicTaskInstance.getCreateTime());
            put("历史任务办理人", historicTaskInstance.getAssignee());
            put("历史执行实例ID", historicTaskInstance.getExecutionId());
            put("历史流程实例ID", historicTaskInstance.getProcessInstanceId());
            put("历史流程定义ID", historicTaskInstance.getProcessDefinitionId());
            put("历史任务结束时间", historicTaskInstance.getEndTime());
        }};
    }

    protected static Map<String, Object> historicVariableInstanceToJSON(HistoricVariableInstance historicVariableInstance) {
        return new LinkedHashMap<String, Object>() {{
            put("历史参数ID", historicVariableInstance.getId());
            put("历史参数名称", historicVariableInstance.getVariableName());
            put("历史参数值", historicVariableInstance.getValue());
            put("历史参数创建时间", historicVariableInstance.getCreateTime());
            put("历史参数流程实例ID", historicVariableInstance.getProcessInstanceId());
            put("历史参数任务ID", historicVariableInstance.getTaskId());
        }};
    }

    protected static Map<String, Object> historicActivityInstanceToJSON(HistoricActivityInstance historicActivityInstance) {
        return new LinkedHashMap<String, Object>() {{
            put("历史活动ID", historicActivityInstance.getId());
            put("历史活动名称", historicActivityInstance.getActivityName());
            put("历史活动类型", historicActivityInstance.getActivityType());
            put("历史活动办理人", historicActivityInstance.getAssignee());
            put("历史活动流程实例ID", historicActivityInstance.getProcessInstanceId());
            put("历史活动任务ID", historicActivityInstance.getTaskId());
        }};
    }

    protected static Map<String, Object> activityInstanceToJSON(ActivityInstance activityInstance) {
        return new LinkedHashMap<String, Object>() {{
            put("活动实例ID", activityInstance.getId());
            put("活动实例对应流程实例id", activityInstance.getProcessInstanceId());
            put("活动实例活动id", activityInstance.getActivityId());
        }};
    }

    protected static Map<String, Object> executionToJSON(Execution execution) {
        return new LinkedHashMap<String, Object>() {{
            put("执行实例ID", execution.getId());
            put("执行实例名称", execution.getName());
            put("执行实例对应流程实例id", execution.getProcessInstanceId());
            put("执行实例活动id", execution.getActivityId());
        }};
    }

    protected static Map<String, Object> identityLinkToJSON(IdentityLink identityLink) {
        return new LinkedHashMap<String, Object>() {{
            put("人员ID", identityLink.getUserId());
            put("任务ID", identityLink.getTaskId());
            put("执行实例对应流程实例id", identityLink.getProcessInstanceId());
            put("执行实例活动id", identityLink.getType());
        }};
    }

    protected static Map<String, Object> jobToJSON(Job job) {
        return new LinkedHashMap<String, Object>() {{
            put("定时器ID", job.getId());
            put("定时器到期日期", job.getDuedate());
            put("定时器类型", job.getJobType());
        }};
    }

    protected static <T> Map<String, Double> FlowNodeToJSON(T nodeInfo) {
        return new LinkedHashMap<String, Double>() {{
            String[] keys = {"x", "y", "width", "height"};
            try {
                for (String key : keys) {
                    Double value = (Double) nodeInfo.getClass().getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1)).invoke(nodeInfo);
                    if (!value.equals(0D)) {
                        put(key, value);
                    }

                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
        }};
    }
}
