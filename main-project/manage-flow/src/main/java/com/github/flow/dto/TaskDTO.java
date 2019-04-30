package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TaskDTO {
    @ApiModelProperty("任务ID")
    private String id;
    @ApiModelProperty("任务名称")
    private String name;
    @ApiModelProperty("任务创建时间")
    private Date createTime;
    @ApiModelProperty("任务办理人")
    private String assignee;
    @ApiModelProperty("执行实例ID")
    private String executionId;
    @ApiModelProperty("流程实例ID")
    private String processInstanceId;
    @ApiModelProperty("流程定义ID")
    private String processDefinitionId;
    @ApiModelProperty("是否被挂起。挂起的对象不能再进行流程操作")
    private Boolean isSuspended;
}
