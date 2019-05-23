package com.github.missthee.controller.flowable;


import com.alibaba.fastjson.JSONObject;
import com.github.missthee.tool.Res;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import static com.github.missthee.controller.flowable.FJSON.getBooleanOrDefaultFromJO;
import static com.github.missthee.controller.flowable.FJSON.getStringOrDefaultFromJO;

@RestController
@RequestMapping("flowable/mana")
public class ProcessManaController {
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
    public ProcessManaController(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, FormService formService, HistoryService historyService, IdentityService identityService, ManagementService managementService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.formService = formService;
        this.historyService = historyService;
        this.identityService = identityService;
        this.managementService = managementService;
    }

    //流程部署
    // act_re_deployment    流程部署表，此表中的key，name由方法设置
    // act_re_procdef       流程定义，此表中的key，name由bpmn中的设置读取
    @RequestMapping("deployment")
    public Res deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .key("QingJiaLiuCheng01")
                .name("请假流程01")
                .addClasspathResource("processes/DemoProcess.bpmn")
                .deploy();
        return Res.success("act_re_deployment 部署: " + deployment.getId());
    }

    //流程部署（zip）
    @RequestMapping("deployment/zip")
    public Res deployProcessByZip(MultipartFile file, String key, String name) throws IOException {
        key = StringUtils.isEmpty(key) ? "QingJiaLiuCheng01" : key;
        name = StringUtils.isEmpty(name) ? "请假流程01" : name;
        Deployment deployment = repositoryService.createDeployment()
                .key(key)
                .name(name)
                .addZipInputStream(new ZipInputStream(file.getInputStream()))
                .deploy();
        return Res.success("act_re_deployment 部署: " + deployment.getId());
    }

    //查询流程部署信息act_re_deployment
    @RequestMapping("searchProcessDeploy")
    public Res searchProcessDeploy(@RequestBody(required = false) JSONObject bJO) {
        String key = getStringOrDefaultFromJO(bJO, "key", null);
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        //条件
        if (key != null) {
            deploymentQuery.deploymentKey(key);
        }
        //排序
        deploymentQuery.orderByDeploymentName().asc();
        deploymentQuery.orderByDeploymenTime().desc();
        //结果集
        List<Deployment> list = deploymentQuery.list();
        List deploymentList = list.stream().map(FJSON::deploymentToJSON).collect(Collectors.toList());
        return Res.success(deploymentList);
    }

    //查询流程定义信息act_re_procdef
    @RequestMapping("searchProcessDefinition")
    public Res searchProcessDefinition(@RequestBody(required = false) JSONObject bJO) {
        String key = getStringOrDefaultFromJO(bJO, "key", "");
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //条件
        if (key != null) {
            processDefinitionQuery.processDefinitionKey(key);//根据流程定义key查询
        }
        //排序
        processDefinitionQuery.orderByProcessDefinitionName().asc();
        processDefinitionQuery.orderByProcessDefinitionVersion().desc();
        //结果集
        List<ProcessDefinition> list = processDefinitionQuery.list();
        List processDefinitionList = list.stream().map(FJSON::processDefinitionToJSON).collect(Collectors.toList());
        return Res.success(processDefinitionList);
    }

    //删除流程定义信息act_re_deployment
    @RequestMapping("deleteProcessDef")
    public Res deleteProcessDef(@RequestBody(required = false) JSONObject bJO) {
        String id = getStringOrDefaultFromJO(bJO, "id", null);
        Boolean forceDelete = getBooleanOrDefaultFromJO(bJO, "forceDelete", false);
        //根据流程部署id删除流程定义。
        //true  ：如果当前id的流程正在执行，则会报错，无法删除
        //false : 如果当前id的流程正在执行，则会把此流程相关信息都删除，包含act_ru_*,act_hi_*等
        repositoryService.deleteDeployment(id, forceDelete);
        return Res.success("成功");
    }

    //修改流程定义信息act_re_deployment
    //使用流程部署方法，修改流程图之后，保持key不变，再次部署，即可更新

    //查询流程图片(流程定义id)
    @RequestMapping("imgByProcessDefId")
    public void imgByProcessId(HttpServletResponse httpServletResponse, @RequestBody(required = false) JSONObject bJO) throws IOException {
        String id = getStringOrDefaultFromJO(bJO, "id", null);
        InputStream processDiagramInputStream = repositoryService.getProcessDiagram(id);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        int len;
        byte[] buff = new byte[100];
        while ((len = processDiagramInputStream.read(buff)) > 0) {
            outputStream.write(buff, 0, len);
            outputStream.flush();
        }
        outputStream.close();
    }

    //查询所有审批流程的最新流程
    @RequestMapping("searchNewestProcessDefinition")
    public Res searchNewestProcessDefinition() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        List processDefList = list.stream().map(FJSON::processDefinitionToJSON).collect(Collectors.toList());
        return Res.success(processDefList);
    }

    //删除指定key所有版本流程定义
    @RequestMapping("deleteProcessDefinitionByKey")
    public Res deleteProcessDefinitionByKey(@RequestBody(required = false) JSONObject bJO) {
        String key = getStringOrDefaultFromJO(bJO, "key", null);
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).list();
        List<String> deploymentIdList = list.stream().map(ProcessDefinition::getDeploymentId).collect(Collectors.toList());
        for (String deploymentId : deploymentIdList) {
            repositoryService.deleteDeployment(deploymentId);
        }
        return Res.success("完成删除");
    }
}