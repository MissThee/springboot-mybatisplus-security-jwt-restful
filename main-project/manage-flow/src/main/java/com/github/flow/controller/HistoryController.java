package com.github.flow.controller;

import com.github.common.tool.Res;
import com.github.flow.dto.HistoricProcessInstanceDTO;
import com.github.flow.dto.HistoricTaskInstanceDTO;
import com.github.flow.dto.HistoricVariableInstanceDTO;
import com.github.flow.vo.HistoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.flowable.variable.api.history.HistoricVariableInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@Api(tags = "工作流-历史")
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("flowable/history")
public class HistoryController {
    private final MapperFacade mapperFacade;
    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService, MapperFacade mapperFacade) {
        this.historyService = historyService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "历史任务-查询多个")
    @PostMapping("task")
    public Res<HistoryVO.SearchHistoryTaskRes> searchHistoryTask(@RequestBody HistoryVO.SearchHistoryTaskReq req) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        boolean hasCondition = false;
        if (req.getAssignee() != null) {
            historicTaskInstanceQuery.taskAssignee(req.getAssignee());
            hasCondition = true;
        }
        if (req.getProcessDefinitionId() != null) {
            historicTaskInstanceQuery.processDefinitionId(req.getProcessDefinitionId());
            hasCondition = true;
        }
        if (req.getProcessDefinitionKey() != null) {
            historicTaskInstanceQuery.processDefinitionKey(req.getProcessDefinitionKey());
            hasCondition = true;
        }
        if (req.getProcessInstanceId() != null) {
            historicTaskInstanceQuery.processInstanceId(req.getProcessInstanceId());
            hasCondition = true;
        }
        if (req.getProcessInstanceBusinessKey() != null) {
            historicTaskInstanceQuery.processInstanceBusinessKey(req.getProcessInstanceBusinessKey());
            hasCondition = true;
        }
        if (req.getTaskCompletedAfter() != null) {
            historicTaskInstanceQuery.taskCompletedAfter(req.getTaskCompletedAfter());
            hasCondition = true;
        }
        if (req.getTaskCompletedBefore() != null) {
            historicTaskInstanceQuery.taskCompletedBefore(req.getTaskCompletedBefore());
            hasCondition = true;
        }
        if (!hasCondition) {
            throw new MissingFormatArgumentException("缺少查询条件");
        }
        historicTaskInstanceQuery
                .orderByProcessInstanceId().asc()
                .orderByExecutionId().asc()
                .orderByHistoricTaskInstanceEndTime().asc();
        long total = historicTaskInstanceQuery.count();
        List<HistoricTaskInstance> list = historicTaskInstanceQuery.list();
        List<HistoricTaskInstanceDTO> hisTaskList = list.stream().map(e -> mapperFacade.map(e, HistoricTaskInstanceDTO.class)).collect(Collectors.toList());
        return Res.success(new HistoryVO.SearchHistoryTaskRes().setTotal(total).setHisTaskList(hisTaskList));
    }

    @ApiOperation(value = "历史变量-查询")
    @PostMapping("variable")
    public Res<HistoryVO.GetHistoryVariableRes> getHistoryVariable(@RequestBody HistoryVO.GetHistoryVariableReq req) {
        HistoricVariableInstanceQuery historicVariableInstanceQuery = historyService.createHistoricVariableInstanceQuery();
        boolean hasCondition = false;
        if (req.getTaskId() != null) {
            historicVariableInstanceQuery.taskId(req.getTaskId());
            hasCondition = true;
        }
        if (req.getExecutionId() != null) {
            historicVariableInstanceQuery.executionId(req.getExecutionId());
            hasCondition = true;
        }
        if (req.getProcessInstanceId() != null) {
            historicVariableInstanceQuery.processInstanceId(req.getProcessInstanceId());
            hasCondition = true;
        }
        if (!hasCondition) {
            throw new MissingFormatArgumentException("缺少查询条件");
        }
        historicVariableInstanceQuery
                .orderByProcessInstanceId().asc();
        long total = historicVariableInstanceQuery.count();
        List<HistoricVariableInstance> list = historicVariableInstanceQuery.listPage(req.getPageIndex(), req.getPageSize());
        List<HistoricVariableInstanceDTO> hisVarList = list.stream().map(e -> mapperFacade.map(e, HistoricVariableInstanceDTO.class)).collect(Collectors.toList());
        HistoryVO.GetHistoryVariableRes getHistoryVariableRes = new HistoryVO.GetHistoryVariableRes().setHisVariableList(hisVarList).setTotal(total);
        return Res.success(getHistoryVariableRes);
    }

    @ApiOperation(value = "历史流程实例-查询")
    @PostMapping("process")
    public Res<HistoryVO.SearchHistoryProcessRes> searchHistoryProcess(@RequestBody @Validated HistoryVO.SearchHistoryProcessReq req) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        boolean hasCondition = false;
        if (req.getInvolvedUser() != null) {
            historicProcessInstanceQuery.involvedUser(req.getInvolvedUser());
            hasCondition = true;
        }
        if (!hasCondition) {
            throw new MissingFormatArgumentException("缺少查询条件");
        }
        historicProcessInstanceQuery
                .orderByProcessDefinitionId().asc();
        long total = historicProcessInstanceQuery.count();
        List<HistoricProcessInstance> list = historicProcessInstanceQuery.listPage(req.getPageIndex(), req.getPageSize());
        List<HistoricProcessInstanceDTO> hisTaskList = list.stream().map(e -> mapperFacade.map(e, HistoricProcessInstanceDTO.class)).collect(Collectors.toList());
        HistoryVO.SearchHistoryProcessRes searchHistoryProcessRes = new HistoryVO.SearchHistoryProcessRes().setHisTaskList(hisTaskList).setTotal(total);
        return Res.success(searchHistoryProcessRes);
    }

//    @ApiIgnore("暂不使用")
//    @ApiOperation(value = "流程历史-活动实例节点-查询")
//    @PostMapping("activity")
//    public Res searchHistoryActivity(@RequestBody(required = false) JSONObject bJO) {
//        String processId = getStringOrDefaultFromJO(bJO, "processId", null);
//        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
//        if (processId != null) {
//            historicActivityInstanceQuery.processInstanceId(processId);
//        }
//        List<HistoricActivityInstance> list = historicActivityInstanceQuery
//                .orderByProcessInstanceId().asc()
//                .orderByExecutionId().asc()
//                .list();
//        List hisTaskList = list.stream().map(FJSON::historicActivityInstanceToJSON).collect(Collectors.toList());
//        return Res.success(hisTaskList);
//    }
}




