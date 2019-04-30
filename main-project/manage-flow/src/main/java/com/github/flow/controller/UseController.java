package com.github.flow.controller;

import com.github.common.tool.Res;
import com.github.flow.dto.IdentityLinkDTO;
import com.github.flow.dto.TaskDTO;
import com.github.flow.vo.UseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "工作流-审批流程流转")
@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping("flowable/use")
public class UseController {
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
    private final MapperFacade mapperFacade;

    @Autowired
    public UseController(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, FormService formService, HistoryService historyService, IdentityService identityService, ManagementService managementService, @Qualifier("processEngine") ProcessEngine processEngine, MapperFacade mapperFacade) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.formService = formService;
        this.historyService = historyService;
        this.identityService = identityService;
        this.managementService = managementService;
        this.processEngine = processEngine;
        this.mapperFacade = mapperFacade;
    }
//流程中的对象简介：
    //1、流程定义。当流程图被部署之后，查询出来的数据。仅为定义的流程，没有实际执行。
//    ProcessDefinition processDefinition;
    //2、流程定义的执行实例。
//    ProcessInstance processInstance;
    //3、描述流程执行的每一个执行节点。ProcessDefinition分支时，ProcessDefinition有一个ProcessInstance；有分支时则有多个
//    Execution execution;
    //4、任务实例，如果任务为userTask，则Execution会有一个task实例
//    Task task;


    // act_ru_task          启动流程中的任务，仅存放正在执行的任务，执行完的任务不在本表。
    // act_ru_identitylink  存放正在执行任务的，办理人信息
    @ApiOperation(value = "任务-查询多个，待执行", notes = "（按办理人查询，按候选办理人查询，按候选办理组查询）")
    @PostMapping("task")
    public Res<UseVO.SearchTaskRes> searchTask(@RequestBody @Validated UseVO.SearchTaskReq req) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (req.getAssignee() != null) {
            taskQuery.taskAssignee(req.getAssignee());//按办理人查询
        }
        if (req.getCandidateUser() != null) {
            taskQuery.taskCandidateOrAssigned(req.getCandidateUser());//按候选办理人查询。
//            taskQuery.taskCandidateUser(candidateUser);//按候选办理人查询。仅无办理人，且有候选人的任务可查到
        }
        if (req.getCandidateGroup() != null && req.getCandidateGroup().size() > 0) {
            taskQuery.taskCandidateGroupIn(req.getCandidateGroup());//按候选组查询。
        }
        if (req.getIsOnlyUnassigned()) {
            taskQuery.taskUnassigned();
        }
        taskQuery
                .orderByProcessInstanceId().asc()
                .orderByTaskCreateTime().desc();
        long total = taskQuery.count();
        List<Task> list = taskQuery.listPage(req.getPageIndex() * req.getPageSize(), (req.getPageIndex() + 1) * req.getPageSize());
        List<TaskDTO> taskList = list.stream().map(e ->{
            TaskDTO taskDTO = mapperFacade.map(e, TaskDTO.class);
            taskDTO.setIsSuspended(e.isSuspended());
            return taskDTO;
        }).collect(Collectors.toList());
        UseVO.SearchTaskRes searchTaskRes = new UseVO.SearchTaskRes()
                .setTaskList(taskList)
                .setTotal(total);
        return Res.success(searchTaskRes);
    }

    @ApiOperation(value = "流程-查询是否完结", notes = "")
    @PostMapping("process/isfinished")
    public Res processIsFinished(@RequestBody(required = false) UseVO.ProcessIsFinishedReq req) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(req.getProcessInstanceId())
                .singleResult();
        if (historicProcessInstance == null) {
            return Res.failure("流程不存在");
        }
        UseVO.ProcessIsFinishedRes processIsFinishedRes = new UseVO.ProcessIsFinishedRes().setIsFinished(historicProcessInstance.getEndTime() != null);
        return Res.success(processIsFinishedRes);
    }
}




