package com.github.flow.controller;


import com.github.common.config.exception.custom.MyMethodArgumentNotValidException;
import com.github.common.tool.Res;
import com.github.flow.dto.ProcessDefinitionDTO;
import com.github.flow.vo.ManaVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.job.api.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

@Api(tags = "工作流-流程部署管理")
//管理流程相关方法
@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping("flowable/mana")
public class ManaController {
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
    public ManaController(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService, FormService formService, HistoryService historyService, IdentityService identityService, ManagementService managementService, @Qualifier("processEngine") ProcessEngine processEngine, MapperFacade mapperFacade) {
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
    //5、历史（历史记录中各节点实例记录）
//    ActivityInstance activityInstance


    // act_re_deployment    流程部署表，此表中的key，name由方法设置
    // act_re_procdef       流程定义，此表中的key，name由bpmn中的设置读取，相同key的流程会归为同一种流程，并增加版本号
    @ApiOperation(value = "流程定义信息-添加（使用bpmn的zip包）", notes = "")
    @PutMapping("deployment/zip")
    public Res<ManaVO.DeployProcessByZipRes> deployProcessByZip(MultipartFile file, String key, String name) throws IOException {
        String dateNow = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now());
        key = StringUtils.isEmpty(key) ? ("UnnamedDeploy" + dateNow) : key;//本次部署的key
        name = StringUtils.isEmpty(name) ? ("未命名部署" + dateNow) : name;//本次部署的name
        Deployment deployment = repositoryService.createDeployment()
                .key(key)
                .name(name)
                .addZipInputStream(new ZipInputStream(file.getInputStream()))
//                .addClasspathResource("processes-no-auto/DemoProcess.bpmn")//使用resources目录下的流程配置文件
                .deploy();
        return Res.success(new ManaVO.DeployProcessByZipRes().setDeploymentId(deployment.getId()));
    }

    // act_re_procdef
    @ApiOperation(value = "流程定义信息-查询多个（同一key）")
    @PostMapping("processdefinition")
    public Res<ManaVO.SearchProcessDefinitionRes> searchProcessDefinition(@RequestBody ManaVO.SearchProcessDefinitionReq req) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //条件
        if (req.getProcessDefinitionKey() != null) {
            processDefinitionQuery.processDefinitionKey(req.getProcessDefinitionKey());//根据流程定义key查询
        }
        List<ProcessDefinition> list = processDefinitionQuery
                .orderByProcessDefinitionKey().asc()//排序
                .orderByProcessDefinitionVersion().desc()
                .orderByProcessDefinitionName().asc()
                .list();//结果集
        List<ProcessDefinitionDTO> processDefinitionList = list.stream().map(e -> {
            ProcessDefinitionDTO processDefinitionDTO = mapperFacade.map(e, ProcessDefinitionDTO.class);
            processDefinitionDTO.setIsSuspended(e.isSuspended());
            return processDefinitionDTO;
        }).collect(Collectors.toList());
        return Res.success(new ManaVO.SearchProcessDefinitionRes().setProcessDefinitionList(processDefinitionList));
    }

    //修改流程定义信息act_re_deployment
    //使用流程部署方法，修改流程图之后，保持key不变，再次部署，即可更新

    @ApiOperation(value = "流程定义信息-删除多个(同一key)、删除单个(id)")
    @DeleteMapping("processdefinition")
    @Transactional(rollbackFor = Exception.class)
    public Res deleteProcessDefinitionByKey(@RequestBody @Validated ManaVO.DeleteProcessDefinitionByKeyReq req) throws MyMethodArgumentNotValidException {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (!StringUtils.isEmpty(req.getProcessDefinitionId())) {
            processDefinitionQuery.processDefinitionId(req.getProcessDefinitionId());
        }
        if (!StringUtils.isEmpty(req.getProcessDefinitionKey())) {
            processDefinitionQuery.processDefinitionKey(req.getProcessDefinitionKey());
        } else {
            throw new MyMethodArgumentNotValidException("缺少条件。需要processDefinitionId或processDefinitionKey");
        }
        List<ProcessDefinition> list = processDefinitionQuery.list();
        List<String> deploymentIdList = list.stream().map(ProcessDefinition::getDeploymentId).collect(Collectors.toList());
        for (String deploymentId : deploymentIdList) {
            try {
                //根据流程部署id删除流程定义。
                //true  ：如果当前id的流程正在执行，则会报错，无法删除
                //false : 如果当前id的流程正在执行，则会把此流程相关信息都删除，包含act_ru_*,act_hi_*等
                repositoryService.deleteDeployment(deploymentId, req.getIsForceDelete());
            } catch (Exception e) {
                e.printStackTrace();
                return Res.failure("失败，该流程定义可能被使用中");
            }
        }
        return Res.success("完成删除");
    }

    @ApiOperation(value = "流程定义-挂起/激活", notes = "使其不能再使用（挂起流程定义，不能再新建实例；挂起实例，实例不能再操作）")
    @PatchMapping("processdefinition/state")
    public Res operateProcessDefinitionById(@RequestBody @Validated ManaVO.OperateProcessDefinitionByIdReq req) throws MyMethodArgumentNotValidException {
        if ("suspend".equals(req.getOperation())) {
            repositoryService.suspendProcessDefinitionById(req.getProcessDefinitionId(), req.getIsOperateRunningInstance(), req.getOperateDate());
            return Res.success("完成挂起");
        } else if ("activate".equals(req.getOperation())) {
            repositoryService.activateProcessDefinitionById(req.getProcessDefinitionId(), req.getIsOperateRunningInstance(), req.getOperateDate());
            return Res.success("完成激活");
        } else {
            //已在注解中完成校验，此处异常不会激活
            throw new MyMethodArgumentNotValidException("操作参数有误。仅能为：suspend,activate");
        }
    }
}