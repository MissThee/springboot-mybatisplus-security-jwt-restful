//package com.github.missthee.controller.example.flowable;
//
//import org.flowable.bpmn.converter.BpmnXMLConverter;
//import org.flowable.bpmn.model.*;
//import org.flowable.bpmn.model.Process;
//import org.flowable.common.engine.impl.util.io.InputStreamSource;
//import org.flowable.engine.ProcessEngine;
//import org.flowable.engine.RepositoryService;
//import org.flowable.engine.impl.RepositoryServiceImpl;
//import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
//import org.flowable.engine.repository.ProcessDefinition;
//import org.flowable.image.impl.DefaultProcessDiagramCanvas;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.InputStream;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.*;
//
//@RestController
//public class test {
//    private final ProcessEngine processEngine;
//    @Autowired
//    RepositoryService repositoryService;
//
//    @Autowired
//    public test(@Qualifier("processEngine") ProcessEngine processEngine) {
//        this.processEngine = processEngine;
//    }
//
//    @RequestMapping("/getProcessTrace")
//    @ResponseBody
//    /*
//      获取各个节点的具体的信息
//      @param wfKey 流程定义的key
//     */
//    public List<Map<String, Object>> getProcessTrace(String wfKey) throws Exception {
//        List<Map<String, Object>> activityInfos = new ArrayList<>();
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(wfKey).latestVersion().singleResult();
//        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(pd.getId());
//        List<ActivityImpl> activitiList = processDefinition.getActivities();
//        InputStream xmlIs = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
//        BpmnModel bm = new BpmnXMLConverter().convertToBpmnModel(new InputStreamSource(xmlIs), false, true);
//
//        // 下方使用反射获取最小的x和y，仔细看就会发现调用的是上方2.1节的方法</strong></span>
//        Class<?> clazz = Class.forName("org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator");
//        Method method = clazz.getDeclaredMethod("initProcessDiagramCanvas", BpmnModel.class);
//        method.setAccessible(true);
//        DefaultProcessDiagramCanvas pdc = (DefaultProcessDiagramCanvas) method.invoke(clazz.newInstance(), bm); // 调用方法
//        new DefaultProcessDiagramCanvas().initialize();
//        clazz = Class.forName("org.activiti.engine.impl.bpmn.diagram.ProcessDiagramCanvas");
//        Field minXField = clazz.getDeclaredField("minX"); // 得到minX字段
//        Field minYField = clazz.getDeclaredField("minY");
//        minXField.setAccessible(true);
//        minYField.setAccessible(true);
//        int minX = minXField.getInt(pdc);// 最小的x值
//        int minY = minYField.getInt(pdc); // 最小的y的值
//
//
//        minX = minX > 0 ? minX - 5 : 0;   // 此处为什么需要减5，上方2.2中activiti源码中有
//        minY = minY > 0 ? minY - 5 : 0;
//        for (ActivityImpl activity : activitiList) {
//            Map<String, Object> activityInfo = new HashMap<>();
//            activityInfo.put("width", activity.getWidth());
//            activityInfo.put("height", activity.getHeight());
//            activityInfo.put("x", activity.getX() - minX);
//            activityInfo.put("y", activity.getY() - minY);
//            activityInfo.put("actId", activity.getId());
//            activityInfo.put("name", activity.getProperty("name")); // ActivityImpl 中没有getName方法，所以此处需要这样获取。
//            activityInfos.add(activityInfo);
//        }
//        return activityInfos;
//    }
//
//    private void test(String processDefinitionId) {
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
//        Iterator var13 = bpmnModel.getPools().iterator();
//
//        while (var13.hasNext()) {
//            Pool pool = (Pool) var13.next();
//            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
//        }
//    }
//
//}
