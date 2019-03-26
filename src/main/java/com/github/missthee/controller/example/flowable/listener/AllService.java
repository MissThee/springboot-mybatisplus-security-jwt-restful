package com.github.missthee.controller.flowable.listener;

import org.flowable.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AllService {
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

    @Autowired
    public AllService(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, FormService formService, HistoryService historyService, IdentityService identityService, ManagementService managementService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.formService = formService;
        this.historyService = historyService;
        this.identityService = identityService;
        this.managementService = managementService;
    }
}
