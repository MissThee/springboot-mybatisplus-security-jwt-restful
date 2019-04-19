package com.github.letter.controller.flowable;


import com.alibaba.fastjson.JSONObject;
import com.github.common.tool.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.job.api.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import static com.github.letter.controller.flowable.FJSON.getBooleanOrDefaultFromJO;
import static com.github.letter.controller.flowable.FJSON.getStringOrDefaultFromJO;

@Api(tags = "审批-流程部署管理")
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
//    @PostMapping("deployment")
//    public Res deployProcess() {
//        Deployment deployment = repositoryService.createDeployment()
//                .key("LiuCheng01")
//                .name("流程01")
//                .addClasspathResource("processes-no-auto/DemoProcess.bpmn")
//                .deploy();
//        return Res.success("id: " + deployment.getId());
//    }

    // act_re_deployment    流程部署表，此表中的key，name由方法设置
    // act_re_procdef       流程定义，此表中的key，name由bpmn中的设置读取，相同key的流程会归为同一类，并增加版本号
    @ApiOperation(value = "部署流程（使用bpmn的zip包）", notes = "")
    @PostMapping("deployment/zip")
    public Res deployProcessByZip(MultipartFile file, String key, String name) throws IOException {
        if (StringUtils.isEmpty(key)) {
            return Res.failure("empty key");
        }
        if (StringUtils.isEmpty(name)) {
            return Res.failure("empty name");
        }
        String dateNow = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now());
        key = StringUtils.isEmpty(key) ? ("DeployAction" + dateNow) : key;//本次部署动作的key
        name = StringUtils.isEmpty(name) ? ("部署动作" + dateNow) : name;//本次部署动作的name
        Deployment deployment = repositoryService.createDeployment()
                .key(key)
                .name(name)
                .addZipInputStream(new ZipInputStream(file.getInputStream()))
                .deploy();
        return Res.success("id: " + deployment.getId());
    }


    // act_re_deployment
    @ApiOperation(value = "查询单个部署信息", notes = "")
    @PostMapping("searchProcessDeploy")
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

    // act_re_procdef
    @ApiOperation(value = "查询单个流程定义信息", notes = "")
    @PostMapping("searchProcessDefinition")
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

    @ApiOperation(value = "删除流程定义信息（单个，提供id）", notes = "")
    @PostMapping("deleteProcessDef")
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

    @ApiOperation(value = "查询单个流程定义的图片", notes = "")
    @PostMapping("imgByProcessDefId")
    public void imgByProcessId(HttpServletResponse httpServletResponse, @RequestBody(required = false) JSONObject bJO) throws IOException {
        String id = getStringOrDefaultFromJO(bJO, "id", null);
        InputStream processDiagramInputStream = repositoryService.getProcessDiagram(id);
        Res.out(httpServletResponse, processDiagramInputStream);
    }

    @ApiOperation(value = "查询所有流程定义的最新版信息", notes = "")
    @PostMapping("searchNewestProcessDefinition")
    public Res searchNewestProcessDefinition() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        List processDefList = list.stream().map(FJSON::processDefinitionToJSON).collect(Collectors.toList());
        return Res.success(processDefList);
    }

    @ApiOperation(value = "删除流程定义信息（多个，提供key）", notes = "")
    @PostMapping("deleteProcessDefinitionByKey")
    public Res deleteProcessDefinitionByKey(@RequestBody(required = false) JSONObject bJO) {
        String key = getStringOrDefaultFromJO(bJO, "key", null);
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).list();
        List<String> deploymentIdList = list.stream().map(ProcessDefinition::getDeploymentId).collect(Collectors.toList());
        for (String deploymentId : deploymentIdList) {
            repositoryService.deleteDeployment(deploymentId);
        }
        return Res.success("完成删除");
    }

    @ApiOperation(value = "挂起指定流程定义", notes = "使其不能再使用（挂起流程定义，不能再新建实例；挂起实例，实例不能再操作）")
    @PostMapping("suspendProcessDefinitionById")
    public Res suspendProcessDefinitionById(@RequestBody(required = false) JSONObject bJO) {
        String id = getStringOrDefaultFromJO(bJO, "id", null);
        //挂起流程实例的id，是否将其正在运行的实例挂起，挂起日期（null则为立即挂起）
        repositoryService.suspendProcessDefinitionById(id, false, null);
        return Res.success("完成挂起");
    }

    @ApiOperation(value = "激活指定流程定义", notes = "")
    @PostMapping("activateProcessDefinitionById")
    public Res activateProcessDefinitionById(@RequestBody(required = false) JSONObject bJO) {
        String id = getStringOrDefaultFromJO(bJO, "id", null);
        //激活流程实例的id，是否将其正在运行的实例激活，激活日期（null则为立即挂起）
        repositoryService.activateProcessDefinitionById(id, false, null);
        return Res.success("完成激活");
    }


    @ApiOperation(value = "查询所有定时任务", notes = "")
    @PostMapping("searchTimerJob")
    public Res searchTimerJob() {
        List<Job> jobList = managementService.createTimerJobQuery().list();
        List<Map<String, Object>> list = jobList.stream().map(FJSON::jobToJSON).collect(Collectors.toList());
        return Res.success(list);
    }
}