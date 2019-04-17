package com.github.letter.controller.flowable;

import com.alibaba.fastjson.JSONObject;
import com.github.common.tool.Res;
import io.swagger.annotations.Api;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.DiagramLayout;
import org.flowable.engine.repository.DiagramNode;
import org.flowable.engine.runtime.*;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.flowable.variable.api.history.HistoricVariableInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.letter.controller.flowable.FJSON.*;
@Api(tags = "审批-审批流程流转")
@RestController
@RequestMapping("flowable/use")
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
    private final ProcessEngine processEngine;

    @Autowired
    public ProcessUseController(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, FormService formService, HistoryService historyService, IdentityService identityService, ManagementService managementService, @Qualifier("processEngine") ProcessEngine processEngine) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.formService = formService;
        this.historyService = historyService;
        this.identityService = identityService;
        this.managementService = managementService;
        this.processEngine = processEngine;
    }

    //流程定义。当流程图被部署之后，查询出来的数据。仅为定义的流程，没有实际执行。
//    ProcessDefinition processDefinition;
    //流程定义的执行实例。
//    ProcessInstance processInstance;
    //描述流程执行的每一个节点。ProcessDefinition分支时，ProcessDefinition有一个ProcessInstance；有分支时则有多个
//    Execution execution;
    //任务实例，如果任务为userTask，则Execution会有一个task实例
//    Task task;

    //启动流程
    // act_ru_execution     流程启动一次，只要没执行完，就会有数据
    @PostMapping("startProcess")
    public Res<Map<String, Object>> startProcess(@RequestBody(required = false) JSONObject bJO) {
        String processDefKey = getStringOrDefaultFromJO(bJO, "processDefKey", "DemoProcess");
        String businessKey = getStringOrDefaultFromJO(bJO, "businessKey", "审批表单1");
        Map<String, Object> variableMap = getMapOrDefaultFromJO(bJO, "variableMap", null);
//        runtimeService.startProcessInstanceById(processId);
        //参数1：流程定义id
        //参数2：Map<String,Object> 流程变量
//        runtimeService.startProcessInstanceById(processId,map );
        //参数1：流程定义id
        //参数2：String 业务id (可设置此id为业务单号)
//        runtimeService.startProcessInstanceById(processId,businessKey );
        //实际开发中常用一下方法
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processIdOrKey, businessKey);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefKey, businessKey, variableMap);
        return Res.success(processInstanceToJSON(processInstance), "启动成功");
    }

    //查询任务
    // act_ru_task          启动流程中的任务，仅存放正在执行的任务，执行完的任务不在本表。
    // act_ru_identitylink  存放正在执行任务的，办理人信息
    @PostMapping("searchTask")
    public Res searchTask(@RequestBody(required = false) JSONObject bJO) {
        String assignee = getStringOrDefaultFromJO(bJO, "assignee", null);
        String candidateUser = getStringOrDefaultFromJO(bJO, "candidateUser", null);
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (assignee != null) {
            taskQuery.taskAssignee(assignee);//按办理人查询
        }
        if (candidateUser != null) {
            taskQuery.taskCandidateUser(candidateUser);//按候选办理人查询。仅无办理人，且有候选人的任务可查到
        }
        List<Task> list = taskQuery
                .orderByProcessInstanceId().asc()
                .orderByTaskCreateTime().desc()
                .list();
        List taskList = list.stream().map(FJSON::taskToJSON).collect(Collectors.toList());
        return Res.success(taskList, assignee);
    }

    //任务拾取
    @PostMapping("claimTask")
    public Res claimTask(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String assignee = getStringOrDefaultFromJO(bJO, "assignee", null);
        if (StringUtils.isEmpty(taskId)) {
            return Res.failure("empty taskId");
        }
        if (StringUtils.isEmpty(assignee)) {
            return Res.failure("empty assignee");
        }
        //claim 会检查assignee字段是否已有办理人。若有，抛出异常；若没有，设置办理人。
        //setAssignee 直接设置办理人。
        //共同点：除claim会检查是否已有办理人外，两者均可给任意任务（不论有无候选办理人），设置任意办理人（不论设置的办理人是否在候选办理人中）
        taskService.claim(taskId, assignee);
        return Res.success();
    }

    //回退任务拾取
    @PostMapping("returnTask")
    public Res returnTask(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        if (StringUtils.isEmpty(taskId)) {
            return Res.failure("empty taskId");
        }
        taskService.setAssignee(taskId, null);
        return Res.success();
    }

    //查询任务的候选办理人
    @PostMapping("getIdentityLinksForTask")
    public Res getIdentityLinksForTask(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        if (taskId == null) {
            return Res.failure("null taskId");
        }
        //其中办理人会额外加入到查询结果中
        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
        List<Map<String, Object>> list = identityLinkList.stream().map(FJSON::identityLinkToJSON).collect(Collectors.toList());
        return Res.success(list);
    }

    //添加任务的候选办理人
    //act_ru_identitylink
    //增加候选办理人时，每个候选办理人有两条数据，类型分别为participant和candidate。其中participant在设置候选办理人、办理人时均会插入
    //删除候选办理人时，仅删除candidate类的记录。
    @PostMapping("optionCandidateUser")
    public Res addCandidateUser(@RequestBody(required = false) JSONObject bJO) {
        Boolean isAdd = getBooleanOrDefaultFromJO(bJO, "isAdd", null);
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String candidateUser = getStringOrDefaultFromJO(bJO, "candidateUser", null);
        if (isAdd == null) {
            return Res.failure("null isAdd");
        }
        if (StringUtils.isEmpty(taskId)) {
            return Res.failure("empty taskId");
        }
        if (StringUtils.isEmpty(candidateUser)) {
            return Res.failure("empty candidateUser");
        }
        if (isAdd) {
            taskService.addCandidateUser(taskId, candidateUser);
        } else {
            taskService.deleteCandidateUser(taskId, candidateUser);
        }
        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
        List<Map<String, Object>> list = identityLinkList.stream().map(FJSON::identityLinkToJSON).collect(Collectors.toList());
        return Res.success(list);
    }

    //办理任务
    @PostMapping("completeTask")
    public Res completeTask(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String comment = getStringOrDefaultFromJO(bJO, "comment", null);
        Map<String, Object> variableMap = getMapOrDefaultFromJO(bJO, "variableMap", null);
        if (StringUtils.isEmpty(taskId)) {
            return Res.failure("empty taskId");
        }
        if (comment != null) {
            Authentication.setAuthenticatedUserId("设置的批注人id");//批注人为线程绑定变量，需在同一线程内设置批注人信息
            taskService.addComment(taskId, taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId(), comment);
        }
        taskService.complete(taskId, variableMap);
        return Res.success();
    }


    //判断流程是否完成
    @PostMapping("processIsFinished")
    public Res processIsFinished(@RequestBody(required = false) JSONObject bJO) {
        String processInstanceId = getStringOrDefaultFromJO(bJO, "processInstanceId", null);
        if (processInstanceId == null) {
            return Res.failure("null processInstanceId");
        }
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (historicProcessInstance == null) {
            return Res.failure("process not exist");
        }
        return Res.success(new JSONObject() {{
            put("isFinished", historicProcessInstance.getEndTime() != null);
        }});
    }

    //查询历史任务
    @PostMapping("searchHistoryTask")
    public Res searchHistoryTask(@RequestBody(required = false) JSONObject bJO) {
        String assignee = getStringOrDefaultFromJO(bJO, "assignee", null);
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        if (assignee != null) {
            historicTaskInstanceQuery.taskAssignee(assignee);
        }
        List<HistoricTaskInstance> list = historicTaskInstanceQuery
                .orderByProcessInstanceId().asc()
                .orderByExecutionId().asc()
                .list();
        List hisTaskList = list.stream().map(FJSON::historyTaskToJSON).collect(Collectors.toList());
        return Res.success(hisTaskList, assignee);
    }
    //HistoryService
    //act_hi_procinst       历史流程实例。   启动一个流程，记录一条数据
    //act_hi_taskinst       历史任务实例。   完成一个任务，记录一条数据（仅任务节点会记录）
    //act_hi_actinst        历史任务实例。   每有一个节点被使用，记录一条数据（所有节点均记录）
    //act_hi_identitylink   历史任务实例。   每有一个用户进行操作，记录一条数据

    //变量操作开始--------------------------------------------------------------------------
    //操作变量。通过taskId或executionId
    //act_ru_variable   正在执行流程中的变量
    //act_hi_varinst    历史流程中的变量
    @PostMapping("setVariableViaExecution")
    public Res setVariableViaExecution(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String executionId = getStringOrDefaultFromJO(bJO, "executionId", null);
        String variable = getStringOrDefaultFromJO(bJO, "variable", null);
        Map<String, Object> variableMap = getMapOrDefaultFromJO(bJO, "variableMap", null);
        //无变量则新增，有变量则覆盖。变量版本号增加，
        if (taskId != null) {
            if (variable != null) {
                taskService.setVariable(taskId, "自定义变量task", variable);//与流程/执行实例绑定的变量。即使使用taskId设置变量，该变量也会与其对应的流程/执行实例绑定。
//                taskService.setVariableLocal(taskId, "自定义变量task", variable);//与任务绑定的变量。该变量会与任务单独绑定，流程中其他任务无法访问变量，任务执行完后需在历史变量中查询。（不常用）
            }
            if (variableMap != null) {
                taskService.setVariables(taskId, variableMap);
            }
        } else if (executionId != null) {
            if (variable != null) {
                runtimeService.setVariable(executionId, "自定义变量execution", variable);
            }
            if (variableMap != null) {
                runtimeService.setVariables(executionId, variableMap);
            }
        } else {
            return Res.failure("need taskId or executionId");
        }
        return Res.success();
    }

    //获取变量。通过taskId或executionId
    @PostMapping("getVariable")
    public Res getVariable(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String executionId = getStringOrDefaultFromJO(bJO, "executionId", null);
        //无变量则新增，有变量则覆盖。变量版本号增加，
        Object variable;
        Object variables;
        if (taskId != null) {
            variable = taskService.getVariable(taskId, "自定义变量task");
            variables = taskService.getVariables(taskId);
        } else if (executionId != null) {
            variable = runtimeService.getVariable(executionId, "自定义变量execution");
            variables = runtimeService.getVariables(executionId);
        } else {
            return Res.failure("null taskId and executionId");
        }
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("variable", variable);
        stringObjectHashMap.put("variables", variables);
        return Res.success(stringObjectHashMap);
    }

    //获取历史变量。通过taskId或executionId
    @PostMapping("getHistoryVariable")
    public Res<List<Map<String, Object>>> getHistoryVariable(@RequestBody(required = false) JSONObject bJO) {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String executionId = getStringOrDefaultFromJO(bJO, "executionId", null);
        String processInstanceId = getStringOrDefaultFromJO(bJO, "processInstanceId", null);
        //无变量则新增，有变量则覆盖。变量版本号增加，
        HistoricVariableInstanceQuery historicVariableInstanceQuery = historyService.createHistoricVariableInstanceQuery();
        if (taskId != null) {
            historicVariableInstanceQuery.taskId(taskId);
        }
        if (executionId != null) {
            historicVariableInstanceQuery.executionId(executionId);
        }
        if (processInstanceId != null) {
            historicVariableInstanceQuery.processInstanceId(processInstanceId);
        }
        List<HistoricVariableInstance> list = historicVariableInstanceQuery
                .orderByProcessInstanceId().asc()
                .list();
        List<Map<String, Object>> hisVarList = list.stream().map(FJSON::historicVariableInstanceToJSON).collect(Collectors.toList());
        return Res.success(hisVarList);
    }
    //变量操作结束--------------------------------------------------------------------------


    //查询历史流程实例
    @PostMapping("searchHistoryProcess")
    public Res searchHistoryProcess(@RequestBody(required = false) JSONObject bJO) {
        String processInstanceId = getStringOrDefaultFromJO(bJO, "processInstanceId", null);
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        if (processInstanceId != null) {
            historicProcessInstanceQuery.processInstanceId(processInstanceId);
        }
        List<HistoricProcessInstance> list = historicProcessInstanceQuery
                .orderByProcessDefinitionId().asc()
                .list();
        List hisTaskList = list.stream().map(FJSON::historicProcessToJSON).collect(Collectors.toList());
        return Res.success(hisTaskList);
    }

    //查询历史活动实例
    @PostMapping("searchHistoryAct")
    public Res searchHistoryAct(@RequestBody(required = false) JSONObject bJO) {
        String activityId = getStringOrDefaultFromJO(bJO, "activityId", null);
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        if (activityId != null) {
            historicActivityInstanceQuery.activityId(activityId);
        }
        List<HistoricActivityInstance> list = historicActivityInstanceQuery
                .orderByProcessInstanceId().asc()
                .orderByExecutionId().asc()
                .list();
        List hisTaskList = list.stream().map(FJSON::historicActivityInstanceToJSON).collect(Collectors.toList());
        return Res.success(hisTaskList);
    }

    //执行实例，下一步。以节点id（activitiId）和流程实例id（processInstanceId），确定当前节点的执行实例（可能有多个），将执行实例前进一步
    //已知当节点不为UserTask时，无法使用任务查询到当前的节点执行情况，只能通过此方式，获取流程实例，进行操作（遗留问题：当一个节点有1个以上的实行实例时，如何区分不同的实例）
    @PostMapping("triggerExecution")
    public Res searchExecution(@RequestBody(required = false) JSONObject bJO) {
        String activityId = getStringOrDefaultFromJO(bJO, "activityId", null);
        String processInstanceId = getStringOrDefaultFromJO(bJO, "processInstanceId", null);
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery();
        if (activityId == null) {
            return Res.failure();
        }
        //可理解为通过执行实例id与执行实例所在图中的节点id确定一个执行实例
        executionQuery.processInstanceId(processInstanceId);
        executionQuery.activityId(activityId);//activityId为图中，receiveTask的Id属性设置的值，通过此值拿取指点节点上的执行实例
        Execution execution = executionQuery.singleResult();
        if (execution == null) {
            return Res.failure("no execution");
        }
        int fakeValue = 0;
        try {
            Thread.sleep(1000);
            fakeValue = 100;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runtimeService.setVariable(execution.getId(), "测试字段", fakeValue);
        runtimeService.trigger(execution.getId());
        return Res.success();
    }

    //查询任务的流程进度图片
    @PostMapping("imgWithHighLight")
    public void imgWithHighLight(HttpServletResponse httpServletResponse, @RequestBody(required = false) JSONObject bJO) throws IOException {
        Boolean isOnlyLast = getBooleanOrDefaultFromJO(bJO, "isOnlyLast", false);
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String processInstanceId = getStringOrDefaultFromJO(bJO, "processInstanceId", null);
        if (taskId != null) {
            processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
        } else if (processInstanceId != null) {

        } else {
            Res.failure("need taskId or processInstanceId");
        }
        List<String> highLightedActivities = new ArrayList<>();     // 构造已执行的节点ID集合
        List<String> highLightedFlows = new ArrayList<>();          // 构造已执行的路径ID集合
        // 获取流程中已经执行的节点，按照执行倒序排序
        String processDefinitionId = null;
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().desc().list();
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
            if (isOnlyLast) {
                if (highLightedActivities.size() > 0) {
                    highLightedFlows.clear();
                    break;
                }
            }
            if (processDefinitionId == null) {
                processDefinitionId = activityInstance.getProcessDefinitionId();
            }
            switch (activityInstance.getActivityType()) {
                case "sequenceFlow":
                    highLightedFlows.add(activityInstance.getActivityId());
                    break;
                case "startEvent":
                case "userTask":
                case "endEvent":
                default:
                    highLightedActivities.add(activityInstance.getActivityId());
                    break;
            }
        }
        // 获取bpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 使用默认配置获得流程图表生成器，并生成追踪图片字符流
        ProcessDiagramGenerator processDiagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivities, highLightedFlows, "宋体", "宋体", "宋体", null, 1.0D, false);
        Res.out(httpServletResponse, inputStream);
    }

    //查询流程图片
    @PostMapping("img")
    public void img(HttpServletResponse httpServletResponse, @RequestBody(required = false) JSONObject bJO) throws IOException {
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String processInstanceId = getStringOrDefaultFromJO(bJO, "processInstanceId", null);
        if (taskId != null) {
            processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
        } else if (processInstanceId != null) {

        } else {
            Res.failure("need taskId or processInstanceId");
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        InputStream processDiagramInputStream = repositoryService.getProcessDiagram(processInstance.getProcessDefinitionId());
        Res.out(httpServletResponse, processDiagramInputStream);
    }

    //查询流程图片中高亮元素坐标(流程线，流程节点)
    @PostMapping("img/highLightData/all")
    public Res<Map<String, List>> imgHighLightDataAll(@RequestBody(required = false) JSONObject bJO) {
        Boolean isOnlyLast = getBooleanOrDefaultFromJO(bJO, "isOnlyLast", false);
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String processInstanceId = getStringOrDefaultFromJO(bJO, "processInstanceId", null);
        if (taskId != null) {
            processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
        } else if (processInstanceId != null) {

        } else {
            Res.failure("need taskId or processInstanceId");
        }
        List<String> highLightedActivities = new ArrayList<>();     // 构造已执行的节点ID集合
        List<String> highLightedFlows = new ArrayList<>();          // 构造已执行的路径ID集合
        // 获取流程中已经执行的节点，按照执行倒序排序
        String processDefinitionId = null;
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().desc().list();
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
            if (isOnlyLast) {
                if (highLightedActivities.size() > 0) {
                    highLightedFlows.clear();
                    break;
                }
            }
            if (processDefinitionId == null) {
                processDefinitionId = activityInstance.getProcessDefinitionId();
            }
            switch (activityInstance.getActivityType()) {
                case "sequenceFlow":
                    highLightedFlows.add(activityInstance.getActivityId());
                    break;
                case "startEvent":
                case "userTask":
                case "endEvent":
                default:
                    highLightedActivities.add(activityInstance.getActivityId());
                    break;
            }
        }
        // 获取bpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        List<List<Map<String, Double>>> flowLineList = new ArrayList<>();//线集合
        {
            Map<String, List<GraphicInfo>> flowLocationMap = bpmnModel.getFlowLocationMap();
            for (String key : flowLocationMap.keySet()) {
                if (highLightedFlows.contains(key)) {
                    List<Map<String, Double>> flowLineNodeList = new ArrayList<Map<String, Double>>();
                    for (GraphicInfo graphicInfo : flowLocationMap.get(key)) {
                        flowLineNodeList.add(FJSON.FlowNodeToJSON(graphicInfo));
                    }
                    flowLineList.add(flowLineNodeList);
                }
            }
        }
        List<Map<String, Double>> flowNodeList = new ArrayList<>();//节点集合
        {
            Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
            for (String key : locationMap.keySet()) {
                if (highLightedActivities.contains(key)) {
                    GraphicInfo flowNode = locationMap.get(key);
                    flowNodeList.add(FlowNodeToJSON(flowNode));
                }
            }
        }
        Map<String, List> resultMap = new HashMap<String, List>() {{
            put("flowLineList", flowLineList);
            put("flowNodeList", flowNodeList);
        }};
        return Res.success(resultMap);
    }

    //查询流程图片中高亮元素坐标(流程节点)
    @PostMapping("img/highLightData/activity")
    public Res<List<Map<String, Double>>> imgHighLightDataActivity(@RequestBody(required = false) JSONObject bJO) {
        Boolean isOnlyLast = getBooleanOrDefaultFromJO(bJO, "isOnlyLast", false);
        String taskId = getStringOrDefaultFromJO(bJO, "taskId", null);
        String processInstanceId = getStringOrDefaultFromJO(bJO, "processInstanceId", null);
        if (taskId != null) {
            processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
        } else if (processInstanceId != null) {

        } else {
            Res.failure("need taskId or processInstanceId");
        }
        List<String> highLightedActivities = new ArrayList<>();     // 构造已执行的节点ID集合
        // 获取流程中已经执行的节点，按照执行倒序排序
        String processDefinitionId = null;
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().desc().list();
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
            if (isOnlyLast) {
                if (highLightedActivities.size() > 0) {
                    break;
                }
            }
            if (processDefinitionId == null) {
                processDefinitionId = activityInstance.getProcessDefinitionId();
            }
            highLightedActivities.add(activityInstance.getActivityId());
        }
        DiagramLayout processDiagramLayout = repositoryService.getProcessDiagramLayout(processDefinitionId);
        List<Map<String, Double>> flowActivityList = new ArrayList<>();
        for (String highLightedActivity : highLightedActivities) {
            DiagramNode diagramNode = processDiagramLayout.getNode(highLightedActivity);
            if (diagramNode != null) {
                flowActivityList.add(FlowNodeToJSON(diagramNode));
            }
        }
        return Res.success(flowActivityList);
    }
}




