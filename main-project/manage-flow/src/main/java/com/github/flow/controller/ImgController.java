package com.github.flow.controller;

import com.github.common.config.exception.custom.MyMethodArgumentNotValidException;
import com.github.common.tool.Res;
import com.github.flow.vo.ImgVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Api(tags = "审批历史-审批流程流转")
@RestController
@RequestMapping("flowable/img")
public class ImgController {
    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final ProcessEngine processEngine;

    @Autowired
    public ImgController(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, HistoryService historyService, @Qualifier("processEngine") ProcessEngine processEngine) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.historyService = historyService;
        this.processEngine = processEngine;
    }

    @ApiOperation(value = "图片")
    @PostMapping
    public void img(HttpServletResponse httpServletResponse, @RequestBody ImgVO.ImgReq req) throws Exception {
        String processInstanceId;
        if (req.getTaskId() != null) {
            processInstanceId = taskService.createTaskQuery().taskId(req.getTaskId()).singleResult().getProcessInstanceId();
        } else if (req.getProcessInstanceId() != null) {
            processInstanceId = req.getProcessInstanceId();
        } else {
            throw new MyMethodArgumentNotValidException("需要taskId或processInstanceId。 need taskId or processInstanceId.");
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        InputStream processDiagramInputStream = repositoryService.getProcessDiagram(processInstance.getProcessDefinitionId());
        Res.out(httpServletResponse, processDiagramInputStream);
    }

    @ApiOperation(value = "图片-包含进度")
    @PostMapping("progress")
    public void imgWithHighLight(HttpServletResponse httpServletResponse, @RequestBody ImgVO.ImgWithHighLightReq req) throws IOException, MyMethodArgumentNotValidException {
        String processInstanceId;
        if (req.getTaskId() != null) {
            processInstanceId = taskService.createTaskQuery().taskId(req.getTaskId()).singleResult().getProcessInstanceId();
        } else if (req.getProcessInstanceId() != null) {
            processInstanceId = req.getProcessInstanceId();
        } else {
            throw new MyMethodArgumentNotValidException("需要taskId或processInstanceId。 need taskId or processInstanceId.");
        }
        List<String> highLightedActivities = new ArrayList<>();     // 构造已执行的节点ID集合
        List<String> highLightedFlows = new ArrayList<>();          // 构造已执行的路径ID集合
        // 获取流程中已经执行的节点，按照执行倒序排序
        String processDefinitionId = null;
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().desc().list();
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
            if (req.getIsOnlyLast()) {
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

    @ApiOperation(value = "图片-中高亮元素坐标")
    @PostMapping("progress/data")
    public Res<Map<String, List>> imgHighLightDataAll(@RequestBody ImgVO.ImgHighLightDataAllReq req) throws MyMethodArgumentNotValidException {
        String processInstanceId;
        if (req.getTaskId() != null) {
            processInstanceId = taskService.createTaskQuery().taskId(req.getTaskId()).singleResult().getProcessInstanceId();
        } else if (req.getProcessInstanceId() != null) {
            processInstanceId = req.getProcessInstanceId();
        } else {
            throw new MyMethodArgumentNotValidException("需要taskId或processInstanceId。 need taskId or processInstanceId.");
        }
        List<String> highLightedActivities = new ArrayList<>();     // 构造已执行的节点ID集合
        List<String> highLightedFlows = new ArrayList<>();          // 构造已执行的路径ID集合
        // 获取流程中已经执行的节点，按照执行倒序排序
        String processDefinitionId = null;
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().desc().list();
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
            if (req.getIsOnlyLast()) {
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
                    if (req.getIsContainHighLightLine()) {
                        highLightedFlows.add(activityInstance.getActivityId());
                    }
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
        if (highLightedFlows.size() > 0) {
            Map<String, List<GraphicInfo>> flowLocationMap = bpmnModel.getFlowLocationMap();
            for (String key : flowLocationMap.keySet()) {
                if (highLightedFlows.contains(key)) {
                    List<Map<String, Double>> flowLineNodeList = new ArrayList<Map<String, Double>>();
                    for (GraphicInfo graphicInfo : flowLocationMap.get(key)) {
                        flowLineNodeList.add(flowNodeFormat(graphicInfo));
                    }
                    flowLineList.add(flowLineNodeList);
                }
            }
        }
        List<Map<String, Double>> flowNodeList = new ArrayList<>();//节点集合
        if (highLightedActivities.size() > 0) {
            Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
            for (String key : locationMap.keySet()) {
                if (highLightedActivities.contains(key)) {
                    GraphicInfo flowNode = locationMap.get(key);
                    flowNodeList.add(flowNodeFormat(flowNode));
                }
            }
        }
        Map<String, List> resultMap = new HashMap<String, List>() {{
            put("flowLineList", flowLineList);
            put("flowNodeList", flowNodeList);
        }};
        return Res.success(resultMap);
    }

    private static <T> Map<String, Double> flowNodeFormat(T nodeInfo) {
        return new LinkedHashMap<String, Double>() {{
            String[] keys = {"x", "y", "width", "height"};
            try {
                for (String key : keys) {
                    Double value = (Double) nodeInfo.getClass().getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1)).invoke(nodeInfo);
                    if (!value.equals(0D)) {
                        put(key, value);
                    }

                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
        }};
    }

    //图形查询结束--------------------------------------------------------------------------

}
