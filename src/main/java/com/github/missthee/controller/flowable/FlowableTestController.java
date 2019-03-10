package com.github.missthee.controller.flowable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.missthee.tool.Res;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "expense")
public class FlowableTestController {
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RepositoryService repositoryService;
    private final ProcessEngine processEngine;
    private final HistoryService historyService;
    private final ManagementService managementService;
    @Autowired
    public FlowableTestController(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, @Qualifier("processEngine") ProcessEngine processEngine, HistoryService historyService, ManagementService managementService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.processEngine = processEngine;
        this.historyService = historyService;
        this.managementService = managementService;
    }
    /**
     * 添加报销
     * /expense/add1?userId=123&money=123321
     *
     * @param userId    用户Id
     * @param money     报销金额
     * @param descption 描述
     */
    @RequestMapping(value = "add1")
    public Res addExpense1(String userId, Integer money, String descption) {
        //启动流程
        HashMap<String, Object> map = new HashMap<>();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("CATAGORY_PHYSIC_SEAL", map);
        return Res.success(processInstance.getId(), "提交成功");
    }

    /**
     * 添加报销
     * /expense/add?userId=123&money=123321
     *
     * @param userId    用户Id
     * @param money     报销金额
     * @param descption 描述
     */
    @RequestMapping(value = "add")
    public Res addExpense(String userId, Integer money, String descption) {
        //启动流程
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskUser", userId);
        map.put("money", money);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Expense", map);
        return Res.success(processInstance.getId(), "提交成功");
    }

    /**
     * 获取审批管理列表
     * /expense/list?userId=123
     */
    @RequestMapping(value = "/list")
    public Res list(String userId) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
        JSONArray jA = new JSONArray();
        for (Task task : tasks) {
            jA.add(new JSONObject() {
                {
                    put("id", task.getId());
                    put("name", task.getName());
                }
            });
        }
        return Res.success(jA);
    }

    /**
     * 批准
     * /expense/apply?taskId=2507
     */
    @RequestMapping(value = "apply")
    public Res apply(String taskId) {
        checkTaskIsValid(taskId);
        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("outcome", "通过");
        }};
        taskService.complete(taskId, map);
        return Res.success("DONE!");
    }

    /**
     * 拒绝
     */
    @RequestMapping(value = "reject")
    public Res reject(String taskId) {
        checkTaskIsValid(taskId);
        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("outcome", "驳回");
        }};
        taskService.complete(taskId, map);
        return Res.success("DONE!");
    }

    private void checkTaskIsValid(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("流程不存在");
        }
    }

    /**
     * 生成流程图
     * /expense/processDiagram?processId=2501
     *
     * @param processId 任务ID
     */
    @RequestMapping(value = "processDiagram")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engConf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engConf.getProcessDiagramGenerator();
        byte[] buf = new byte[1024];
        int length;
        OutputStream out = httpServletResponse.getOutputStream();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", engConf.getActivityFontName(), engConf.getLabelFontName(), engConf.getAnnotationFontName(), engConf.getClassLoader(), false);
        while ((length = in.read(buf)) != -1) {
            out.write(buf, 0, length);
        }
    }
}
