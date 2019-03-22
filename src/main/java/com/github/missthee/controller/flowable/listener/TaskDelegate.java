package com.github.missthee.controller.flowable.listener;

import com.github.missthee.db.primary.model.basic.User;
import com.github.missthee.service.interf.basic.UserService;
import com.github.missthee.tool.ApplicationContextHolder;
import org.flowable.engine.*;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//图中配置了此类的全路径，不要随意修改本类所属的包
@Component
public class TaskDelegate extends AllService implements JavaDelegate {
    @Autowired
    public TaskDelegate(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, FormService formService, HistoryService historyService, IdentityService identityService, ManagementService managementService) {
        super(runtimeService, taskService, repositoryService, formService, historyService, identityService, managementService);
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {

    }
}
