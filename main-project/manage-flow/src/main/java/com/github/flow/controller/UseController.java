package com.github.flow.controller;

import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.tool.Res;
import com.github.flow.dto.IdentityLinkDTO;
import com.github.flow.dto.ProcessInstanceDTO;
import com.github.flow.dto.TaskDTO;
import com.github.flow.vo.UseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.*;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "工作流-审批流程流转")
@RestController
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


    // act_ru_execution     流程启动一次，只要没执行完，就会有数据
    @ApiOperation(value = "流程-启动", notes = "")
    @PutMapping("process")
    public Res<UseVO.StartProcessRes> startProcess(UseVO.StartProcessReq req) {
//        runtimeService.startProcessInstanceById(processId);//流程定义id
//        runtimeService.startProcessInstanceById(processId,map );//流程定义id，Map<String,Object> 流程变量
//        runtimeService.startProcessInstanceById(processId,businessKey );//流程定义id，String 业务id (可设置此id为业务单号)
//        runtimeService.startProcessInstanceByKey(processKey, businessKey);//流程定义key，Map<String,Object> 流程变量
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(req.getProcessDefKey(), req.getBusinessKey(), req.getVariableMap());
        UseVO.StartProcessRes startProcessRes = new UseVO.StartProcessRes().setProcessInstance(mapperFacade.map(processInstance, ProcessInstanceDTO.class));
        return Res.success(startProcessRes, "启动成功");
    }

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
        List<Task> list = taskQuery.listPage(req.getPageIndex(), req.getPageSize());
        List<TaskDTO> taskList = list.stream().map(e -> mapperFacade.map(e, TaskDTO.class)).collect(Collectors.toList());
        UseVO.SearchTaskRes searchTaskRes = new UseVO.SearchTaskRes()
                .setTaskList(taskList)
                .setTotal(total);
        return Res.success(searchTaskRes);
    }

    @ApiOperation(value = "任务-办理人-添加（任务拾取）")
    @PutMapping("task/assignee")
    public Res claimTask(@RequestBody @Validated UseVO.ClaimTaskReq req) {
        //claim 会检查assignee字段是否已有办理人。若有，抛出异常；若没有，设置办理人。
        //setAssignee 直接设置办理人。
        //共同点：除claim会检查是否已有办理人外，两者均可给任意任务（不论有无候选办理人），设置任意办理人（不论设置的办理人是否在候选办理人中）
        try {
            taskService.claim(req.getTaskId(), req.getAssignee());
        } catch (Exception e) {
            return Res.failure("添加办理人失败，已有办理人");
        }
        return Res.success("添加办理人成功");
    }

    @ApiOperation(value = "任务-办理人-删除（回退任务拾取）")
    @DeleteMapping("task/assignee")
    public Res returnTask(@RequestBody @Validated UseVO.ReturnTaskReq req) {
        taskService.setAssignee(req.getTaskId(), null);
        return Res.success("删除办理人成功");
    }

    @ApiOperation(value = "任务-候选办理人-查询", notes = "")
    @PostMapping("task/candidateuser")
    public Res getCandidateUser(@RequestBody @Validated UseVO.GetCandidateUserReq req) {
        //其中办理人会额外加入到查询结果中
        List<IdentityLink> list = taskService.getIdentityLinksForTask(req.getTaskId());
        List<IdentityLinkDTO> identityLinkList = list.stream().map(e -> mapperFacade.map(e, IdentityLinkDTO.class)).collect(Collectors.toList());
        UseVO.GetCandidateUserRes getCandidateUserRes = new UseVO.GetCandidateUserRes()
                .setIdentityLinkList(identityLinkList);
        return Res.success(getCandidateUserRes);
    }

    //act_ru_identitylink
    //增加候选办理人时，每个候选办理人有两条数据，类型分别为participant（参与过的人）和candidate（候选办理人）。其中participant在设置候选办理人、办理人时均会插入
    @ApiOperation(value = "任务-候选办理人-添加", notes = "")
    @PutMapping("task/candidateuser")
    public Res addCandidateUser(@RequestBody @Validated UseVO.AddCandidateUserReq req) {
        try {
            taskService.addCandidateUser(req.getTaskId(), req.getCandidateUser());
        } catch (Exception e) {
            return Res.success("添加失败");
        }
        return Res.success("添加成功");
    }

    //act_ru_identitylink
    //删除候选办理人时，仅删除candidate类的记录。
    @ApiOperation(value = "任务-候选办理人-删除", notes = "")
    @DeleteMapping("task/candidateuser")
    public Res<List<Map<String, Object>>> deleteCandidateUser(@RequestBody @Validated UseVO.DeleteCandidateUserReq req) {
        try {
            taskService.deleteCandidateUser(req.getTaskId(), req.getCandidateUser());
        } catch (Exception e) {
            return Res.success("删除失败");
        }
        return Res.success("删除成功");
    }

    @ApiOperation(value = "任务-办理", notes = "")
    @PutMapping("task")
    public Res completeTask(HttpServletRequest httpServletRequest, @RequestBody @Validated UseVO.CompleteTaskReq req) throws InterruptedException {
        if (req.getComment() != null) {
            Authentication.setAuthenticatedUserId(JavaJWT.getId(httpServletRequest));//批注人为线程绑定变量，需在同一线程内设置批注人信息。setAuthenticatedUserId的实际实现类中，使用的ThreadLocal保存变量
            String processInstanceId = taskService.createTaskQuery()
                    .taskId(req.getTaskId())
                    .singleResult()
                    .getProcessInstanceId();
            taskService.addComment(req.getTaskId(), processInstanceId, req.getComment());
        }
        taskService.complete(req.getTaskId(), req.getVariableMap());
        return Res.success("操作成功");
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

    //HistoryService
    //act_hi_procinst       历史流程实例。   启动一个流程，记录一条数据
    //act_hi_taskinst       历史任务实例。   完成一个任务，记录一条数据（仅任务节点会记录）
    //act_hi_actinst        历史任务实例。   每有一个节点被使用，记录一条数据（所有节点均记录）
    //act_hi_identitylink   历史任务实例。   每有一个用户进行操作，记录一条数据

    //变量操作开始--------------------------------------------------------------------------
    //act_ru_variable   正在执行流程中的变量
    //act_hi_varinst    历史流程中的变量
    @ApiOperation(value = "流程变量-修改", notes = "通过taskId或executionId")
    @PatchMapping("variable")
    public Res setVariable(@RequestBody UseVO.SetVariableReq req) {

        //无变量则新增，有变量则覆盖。变量版本号增加，
        if (req.getTaskId() != null) {
            if (req.getVariable() != null) {
                taskService.setVariable(req.getTaskId(), req.getVariableName(), req.getVariable());//与流程/执行实例绑定的变量。即使使用taskId设置变量，该变量也会与其对应的流程/执行实例绑定。
//                taskService.setVariableLocal(taskId, "自定义变量task", variable);//与任务绑定的变量。该变量会与任务单独绑定，流程中其他任务无法访问变量，任务执行完后需在历史变量中查询。（不常用）
            }
            if (req.getVariableMap() != null) {
                taskService.setVariables(req.getTaskId(), req.getVariableMap());
            }
        } else if (req.getExecutionId() != null) {
            if (req.getVariable() != null) {
                runtimeService.setVariable(req.getExecutionId(), req.getVariableName(), req.getVariable());
            }
            if (req.getVariableMap() != null) {
                runtimeService.setVariables(req.getExecutionId(), req.getVariableMap());
            }
        } else {
            return Res.failure("修改失败，条件不足");
        }
        return Res.success("修改成功");
    }

    @ApiOperation(value = "流程变量-查询", notes = "通过taskId或executionId")
    @PostMapping("variable")
    public Res getVariable(@RequestBody UseVO.GetVariableReq req) {
        //无变量则新增，有变量则覆盖。变量版本号增加，
        Object variable;
        Object variables;
        if (req.getTaskId() != null) {
            variable = taskService.getVariable(req.getTaskId(), req.getVariableName());
            variables = taskService.getVariables(req.getTaskId());
        } else if (req.getExecutionId() != null) {
            variable = runtimeService.getVariable(req.getExecutionId(), req.getVariableName());
            variables = runtimeService.getVariables(req.getExecutionId());
        } else {
            return Res.failure("查询失败，条件不足");
        }
        UseVO.GetVariableRes getVariableRes = new UseVO.GetVariableRes()
                .setVariable(variable)
                .setVariables(variables);
        return Res.success(getVariableRes);
    }
    //变量操作结束--------------------------------------------------------------------------

//    @ApiIgnore("暂不使用")
//    //已知当节点不为UserTask时，无法使用任务查询到当前的节点执行情况，只能通过此方式，获取流程实例，进行操作（遗留问题：当一个节点有1个以上的实行实例时，如何区分不同的实例）
//    @ApiOperation(value = "执行实例-推进一步", notes = "以节点id（activitiId）和流程实例id（processInstanceId），确定当前节点的执行实例（可能有多个），将执行实例前进一步")
//    @PostMapping("execution/trigger")
//    public Res searchExecution(@RequestBody(required = false) JSONObject bJO) {
//        String activityId = getStringOrDefaultFromJO(bJO, "activityId", null);
//        String processInstanceId = getStringOrDefaultFromJO(bJO, "processInstanceId", null);
//        ExecutionQuery executionQuery = runtimeService.createExecutionQuery();
//        if (activityId == null) {
//            return Res.failure();
//        }
//        //可理解为通过执行实例id与执行实例所在图中的节点id确定一个执行实例
//        executionQuery.processInstanceId(processInstanceId);
//        executionQuery.activityId(activityId);//activityId为图中，receiveTask的Id属性设置的值，通过此值拿取指点节点上的执行实例
//        Execution execution = executionQuery.singleResult();
//        if (execution == null) {
//            return Res.failure("no execution");
//        }
//        int fakeValue = 0;
//        try {
//            Thread.sleep(1000);
//            fakeValue = 100;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        runtimeService.setVariable(execution.getId(), "测试字段", fakeValue);
//        runtimeService.trigger(execution.getId());
//        return Res.success();
//    }
}




