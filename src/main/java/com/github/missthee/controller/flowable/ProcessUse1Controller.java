package com.github.missthee.controller.flowable;

import com.alibaba.fastjson.JSONObject;
import com.github.missthee.tool.Res;
import org.flowable.engine.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.missthee.controller.flowable.JOTool.getStringOrDefaultFromJO;

@RestController
@RequestMapping("flowable/learn1")
public class ProcessUse1Controller {
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
    public ProcessUse1Controller(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, FormService formService, HistoryService historyService, IdentityService identityService, ManagementService managementService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.formService = formService;
        this.historyService = historyService;
        this.identityService = identityService;
        this.managementService = managementService;
    }

    //启动流程
    // act_ru_execution     流程启动一次，只要没执行完，就会有数据
    @RequestMapping("start")
    public Res startProcess(@RequestBody(required = false) JSONObject bJO) {
        String processIdOrKey = getStringOrDefaultFromJO(bJO, "processId", "DemoProcess");
        String businessKey = getStringOrDefaultFromJO(bJO, "businessKey", "审批表单1");
//        runtimeService.startProcessInstanceById(processId);
        //参数1：流程定义id
        //参数2：Map<String,Object> 流程变量
//        runtimeService.startProcessInstanceById(processId,map );
        //参数1：流程定义id
        //参数2：String 业务id (可设置此id为业务单号)
//        runtimeService.startProcessInstanceById(processId,businessKey );
        //实际开发中常用
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processIdOrKey, businessKey);
//        runtimeService.startProcessInstanceByKey(key,businessKey,map );
        return Res.success(processInstance.getBusinessKey(),"启动成功");
    }

    @RequestMapping("queryMyTask")
    public Res queryMyTask(@RequestBody(required = false) JSONObject bJO) {
        String assignee = getStringOrDefaultFromJO(bJO, "assignee", "test1");
        List<Map<String, String>> taskList = taskService.createTaskQuery().taskAssignee(assignee).list().stream().map(JOTool::taskToJSON).collect(Collectors.toList());
        return Res.success(taskList);
    }
}
