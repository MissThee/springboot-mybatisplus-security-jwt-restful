package com.github.missthee.controller.flowable;

import com.alibaba.fastjson.JSONObject;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JOTool {
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

    protected static Map<String, String> deploymentToJSON(Deployment deployment) {
        return new HashMap<String, String>() {{
            put("部署ID", deployment.getId());
            put("部署名称", deployment.getName());
            put("部署KEY", deployment.getKey());
            put("部署时间", deployment.getDeploymentTime().toString());
        }};
    }

    protected static Map<String, String> processDefinitionToJSON(ProcessDefinition processDefinition) {
        return new HashMap<String, String>() {{
            put("部署ID", processDefinition.getId());
            put("部署名称", processDefinition.getName());
            put("部署KEY", processDefinition.getKey());
            put("部署版本", String.valueOf(processDefinition.getVersion()));
        }};
    }

    protected static Map<String, String> taskToJSON(Task task) {
        return new HashMap<String, String>() {{
            put("任务ID", task.getId());
            put("任务名称", task.getName());
            put("流程实例ID", task.getProcessInstanceId());
            put("执行实例ID", task.getExecutionId());
            put("流程定义ID", task.getProcessDefinitionId());
            put("事务办理人", task.getAssignee());
        }};
    }

}
