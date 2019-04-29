package com.github.flow.controller;

import com.github.common.config.exception.custom.MyMethodArgumentNotValidException;
import com.github.common.tool.Res;
import com.github.flow.common.FUtils;
import com.github.flow.dto.FlowLinePositionDTO;
import com.github.flow.dto.FlowNodePositionDTO;
import com.github.flow.vo.ImgVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
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
import java.util.*;

@Api(tags = "工作流-图片")
@RestController
@RequestMapping("flowable/img")
public class ImgController {
    private final FUtils fUtils;
    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final ProcessEngine processEngine;
    private final MapperFacade mapperFacade;

    @Autowired
    public ImgController(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, HistoryService historyService, @Qualifier("processEngine") ProcessEngine processEngine, MapperFacade mapperFacade, FUtils fUtils) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.historyService = historyService;
        this.processEngine = processEngine;
        this.mapperFacade = mapperFacade;
        this.fUtils = fUtils;
    }

    @ApiOperation(value = "图片")
    @PostMapping
    public void img(HttpServletResponse httpServletResponse, @RequestBody ImgVO.ImgReq req) throws Exception {
        String processDefinitionId;
        if (req.getProcessDefinitionId() != null) {
            processDefinitionId = req.getProcessDefinitionId();
        } else {
            String processInstanceId = fUtils.processInstanceIdOrTaskIdToProcessInstanceId(req.getTaskId(), req.getProcessInstanceId());
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processDefinitionId = processInstance.getProcessDefinitionId();
        }

        InputStream processDiagramInputStream = repositoryService.getProcessDiagram(processDefinitionId);
        Res.out(httpServletResponse, processDiagramInputStream);
    }

    @ApiOperation(value = "图片-包含进度")
    @PostMapping("progress")
    public void imgWithHighLight(HttpServletResponse httpServletResponse, @RequestBody ImgVO.ImgWithHighLightReq req) throws IOException, MyMethodArgumentNotValidException {
        String processInstanceId = fUtils.processInstanceIdOrTaskIdToProcessInstanceId(req.getTaskId(), req.getProcessInstanceId());
        List<String> highLightedActivities = new ArrayList<>();     // 构造已执行的节点ID集合
        List<String> highLightedFlows = new ArrayList<>();          // 构造已执行的路径ID集合
        // 获取流程中已经执行的节点，按照执行倒序排序
        String processDefinitionId = makeActivityInstanceList(processInstanceId, req.getIsOnlyLast(), req.getIsContainHighLightLine(), highLightedActivities, highLightedFlows);
        // 获取bpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 使用默认配置获得流程图表生成器，并生成追踪图片字符流
        ProcessDiagramGenerator processDiagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivities, highLightedFlows, "宋体", "宋体", "宋体", null, 1.0D, false);
        Res.out(httpServletResponse, inputStream);
    }

    @ApiOperation(value = "图片-中高亮元素坐标")
    @PostMapping("progress/data")
    public Res<ImgVO.ImgHighLightDataAllRes> imgHighLightDataAll(@RequestBody ImgVO.ImgHighLightDataAllReq req) throws MyMethodArgumentNotValidException {
        String processInstanceId = fUtils.processInstanceIdOrTaskIdToProcessInstanceId(req.getTaskId(), req.getProcessInstanceId());
        List<String> highLightedActivities = new ArrayList<>();     // 构造已执行的节点ID集合
        List<String> highLightedFlows = new ArrayList<>();          // 构造已执行的路径ID集合
        // 获取流程中已经执行的节点，按照执行倒序排序
        String processDefinitionId = makeActivityInstanceList(processInstanceId, req.getIsOnlyLast(), req.getIsContainHighLightLine(), highLightedActivities, highLightedFlows);
        // 获取bpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        List<List<FlowLinePositionDTO>> flowLineList = new ArrayList<>();//线集合
        if (highLightedFlows.size() > 0) {
            Map<String, List<GraphicInfo>> flowLocationMap = bpmnModel.getFlowLocationMap();
            for (String key : flowLocationMap.keySet()) {
                if (highLightedFlows.contains(key)) {
                    List<FlowLinePositionDTO> flowLineNodeList = new ArrayList<>();
                    for (GraphicInfo flowLine : flowLocationMap.get(key)) {
                        flowLineNodeList.add(mapperFacade.map(flowLine, FlowLinePositionDTO.class));
                    }
                    flowLineList.add(flowLineNodeList);
                }
            }
        }
        List<FlowNodePositionDTO> flowNodeList = new ArrayList<>();//节点集合
        if (highLightedActivities.size() > 0) {
            Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
            for (String key : locationMap.keySet()) {
                if (highLightedActivities.contains(key)) {
                    GraphicInfo flowNode = locationMap.get(key);
                    flowNodeList.add(mapperFacade.map(flowNode, FlowNodePositionDTO.class));
                }
            }
        }
        ImgVO.ImgHighLightDataAllRes imgHighLightDataAllRes = new ImgVO.ImgHighLightDataAllRes()
                .setFlowLineList(flowLineList)
                .setFlowNodeList(flowNodeList);
        return Res.success(imgHighLightDataAllRes);
    }

    private String makeActivityInstanceList(String processInstanceId, boolean isOnlyLast, boolean isContainHighLightLine, List<String> highLightedActivities, List<String> highLightedFlows) {
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
            //集合中包含路径和节点，按照固定类型分类填充数组
            if ("sequenceFlow".equals(activityInstance.getActivityType())) {
                if (isContainHighLightLine) {
                    highLightedFlows.add(activityInstance.getActivityId());
                }
            } else {
                highLightedActivities.add(activityInstance.getActivityId());
            }
        }
        return processDefinitionId;
    }

//    private static <T> Map<String, Double> flowNodeFormat(T nodeInfo) {//已由实体类替代
//        return new LinkedHashMap<String, Double>() {{
//            String[] keys = {"x", "y", "width", "height"};
//            try {
//                for (String key : keys) {
//                    Double value = (Double) nodeInfo.getClass().getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1)).invoke(nodeInfo);
//                    if (!value.equals(0D)) {
//                        put(key, value);
//                    }
//                }
//            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
//            }
//        }};
//    }


}
