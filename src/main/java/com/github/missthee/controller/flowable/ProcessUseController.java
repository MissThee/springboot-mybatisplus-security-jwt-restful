package com.github.missthee.controller.flowable;

import com.alibaba.fastjson.JSONObject;
import com.github.missthee.tool.Res;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.missthee.controller.flowable.JOTool.getStringOrDefaultFromJO;

@RestController
@RequestMapping("flowable/learn")
public class ProcessUseController {
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
    public ProcessUseController(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, FormService formService, HistoryService historyService, IdentityService identityService, ManagementService managementService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.formService = formService;
        this.historyService = historyService;
        this.identityService = identityService;
        this.managementService = managementService;
    }

    //流程定义。当流程图被部署之后，查询出来的数据。仅为定义的流程，没有实际执行
    ProcessDefinition processDefinition;
    //流程定义的执行实例。
    ProcessInstance processInstance;
    //描述流程执行的每一个节点。无并发时，等同于ProcessInstance
    Execution execution;
    //任务实例
    HistoricTaskInstance historicTaskInstance;

    //启动流程
    // act_ru_execution     流程启动一次，只要没执行完，就会有数据
    @RequestMapping("start")
    public Res startProcess(@RequestBody(required = false) JSONObject bJO) {
        String processId = getStringOrDefaultFromJO(bJO, "processId", "DemoProcess:2:870e71fd-43d0-11e9-8c3c-0a0027000008");
        runtimeService.startProcessInstanceById(processId);
        return Res.success("启动成功");
    }

    //查询任务
    // act_ru_task          启动流程中的任务，仅存放正在执行的任务，执行完的任务不在本表。
    // act_ru_identitylink  存放正在执行任务的，办理人信息
    @RequestMapping("search")
    public Res searchTask(@RequestBody(required = false) JSONObject bJO) {
        String userId = getStringOrDefaultFromJO(bJO, "userId", "test1");
        List<Map<String, String>> taskList = taskService.createTaskQuery().taskAssignee(userId).list()
                .stream().map(JOTool::taskToJSON).collect(Collectors.toList());
        return Res.success(taskList);
    }

    //办理任务
    @RequestMapping("completeTask")
    public Res completeTask(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", "b2cf32f9-43d1-11e9-b7c1-0a0027000008");
        taskService.complete(taskId);
        return Res.success("任务完成");
    }

    //HistoryService
    //act_hi_procinst       历史流程实例。   启动一个流程，记录一条数据
    //act_hi_taskinst       历史任务实例。   完成一个任务，记录一条数据（仅任务节点会记录）
    //act_hi_actinst        历史任务实例。   每有一个节点被使用，记录一条数据（所有节点均记录）
    //act_hi_identitylink   历史任务实例。   每有一个用户进行操作，记录一条数据


}
