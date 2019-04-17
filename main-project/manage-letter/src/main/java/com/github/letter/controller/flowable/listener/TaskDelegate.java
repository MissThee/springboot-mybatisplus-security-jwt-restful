package com.github.letter.controller.flowable.listener;

import org.flowable.engine.*;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
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
