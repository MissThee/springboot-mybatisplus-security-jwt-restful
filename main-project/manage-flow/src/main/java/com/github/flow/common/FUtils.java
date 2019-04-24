package com.github.flow.common;

import com.github.common.config.exception.custom.MyMethodArgumentNotValidException;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FUtils {
    private final TaskService taskService;

    @Autowired
    public FUtils(TaskService taskService) {
        this.taskService = taskService;
    }

    public String processInstanceIdOrTaskIdToProcessInstanceId(String taskId, String processInstanceId) throws MyMethodArgumentNotValidException {
        if (taskId != null) {
            return taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
        } else if (processInstanceId != null) {
            return processInstanceId;
        } else {
            throw new MyMethodArgumentNotValidException("需要taskId或processInstanceId。 need taskId or processInstanceId.");
        }
    }
}
