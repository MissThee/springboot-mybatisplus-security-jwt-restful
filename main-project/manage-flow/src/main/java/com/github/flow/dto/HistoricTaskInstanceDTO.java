package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public  class HistoricTaskInstanceDTO {
    @ApiModelProperty("历史任务ID")
    private String id;
    @ApiModelProperty("历史任务名称")
    private String name;
    @ApiModelProperty("历史任务创建时间")
    private Date createTime;
    @ApiModelProperty("历史任务办理人")
    private String assignee;
    @ApiModelProperty("历史执行实例ID")
    private String executionId;
    @ApiModelProperty("历史流程实例ID")
    private String processInstanceId;
    @ApiModelProperty("历史流程定义ID")
    private String processDefinitionId;
    @ApiModelProperty("历史任务结束时间")
    private Date endTime;
}