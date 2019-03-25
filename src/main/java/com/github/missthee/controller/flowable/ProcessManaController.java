package com.github.missthee.controller.flowable;


import com.alibaba.fastjson.JSONObject;
import com.github.missthee.tool.Res;
import lombok.AllArgsConstructor;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.job.api.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import static com.github.missthee.controller.flowable.FJSON.getBooleanOrDefaultFromJO;
import static com.github.missthee.controller.flowable.FJSON.getStringOrDefaultFromJO;

//管理流程相关方法
@RestController
@RequestMapping("flowable/mana")
public class ProcessManaController {
    private final RepositoryService repositoryService;
    private final ManagementService managementService;

    @Autowired
    public ProcessManaController(RepositoryService repositoryService, ManagementService managementService) {
        this.repositoryService = repositoryService;
        this.managementService = managementService;
    }

    //流程部署（使用resources目录下的流程配置文件）
//    @RequestMapping("deployment")
//    public Res deployProcess() {
//        Deployment deployment = repositoryService.createDeployment()
//                .key("LiuCheng01")
//                .name("流程01")
//                .addClasspathResource("processes-no-auto/DemoProcess.bpmn")
//                .deploy();
//        return Res.success("id: " + deployment.getId());
//    }

    //流程部署（使用流程配置打包的zip文件）
    // act_re_deployment    流程部署表，此表中的key，name由方法设置
    // act_re_procdef       流程定义，此表中的key，name由bpmn中的设置读取，相同key的流程会归为同一类，并增加版本号
    @RequestMapping("deployment/zip")
    public Res deployProcessByZip(MultipartFile file, String key, String name) throws IOException {
        if (StringUtils.isEmpty(key)) {
            return Res.failure("empty key");
        }
        if (StringUtils.isEmpty(name)) {
            return Res.failure("empty name");
        }
        key = StringUtils.isEmpty(key) ? "LiuCheng01" : key;
        name = StringUtils.isEmpty(name) ? "流程01" : name;
        Deployment deployment = repositoryService.createDeployment()
                .key(key)
                .name(name)
                .addZipInputStream(new ZipInputStream(file.getInputStream()))
                .deploy();
        return Res.success("id: " + deployment.getId());
    }

    //查询流程部署信息
    // act_re_deployment
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

    //查询流程定义信息
    // act_re_procdef
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
        Res.out(httpServletResponse, processDiagramInputStream);
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

    //挂起指定流程定义，使其不能再使用（挂起流程定义，不能再新建实例；挂起实例，实例不能再操作）
    @RequestMapping("suspendProcessDefinitionById")
    public Res suspendProcessDefinitionById(@RequestBody(required = false) JSONObject bJO) {
        String id = getStringOrDefaultFromJO(bJO, "id", null);
        //挂起流程实例的id，是否将其正在运行的实例挂起，挂起日期（null则为立即挂起）
        repositoryService.suspendProcessDefinitionById(id, false, null);
        return Res.success("完成挂起");
    }

    //激活指定流程定义
    @RequestMapping("activateProcessDefinitionById")
    public Res activateProcessDefinitionById(@RequestBody(required = false) JSONObject bJO) {
        String id = getStringOrDefaultFromJO(bJO, "id", null);
        //激活流程实例的id，是否将其正在运行的实例激活，激活日期（null则为立即挂起）
        repositoryService.activateProcessDefinitionById(id, false, null);
        return Res.success("完成激活");
    }

    //手动执行定时器
    @RequestMapping("searchTimerJob")
    public Res searchTimerJob(@RequestBody(required = false) JSONObject bJO) {
        //激活流程实例的id，是否将其正在运行的实例激活，激活日期（null则为立即挂起）
        List<Job> jobList = managementService.createTimerJobQuery().list();
        List<Map<String, Object>> list = jobList.stream().map(FJSON::jobToJSON).collect(Collectors.toList());
        return Res.success(list);
    }
}