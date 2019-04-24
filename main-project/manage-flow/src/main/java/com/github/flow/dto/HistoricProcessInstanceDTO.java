package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HistoricProcessInstanceDTO {
    @ApiModelProperty("历史流程实例ID")
    private String id;
    @ApiModelProperty("历史流程实例的业务id")
    private String businessKey;
    @ApiModelProperty("历史流程实例名称")
    private String name;
    @ApiModelProperty("历史流程实例开始ID")
    private String startActivityId;
    @ApiModelProperty("历史流程实例结束ID")
    private String endActivityId;
    @ApiModelProperty("历史流程实例开始时间")
    private String startTime;
    @ApiModelProperty("历史流程实例结束时间")
    private String endTime;
    @ApiModelProperty("流程定义ID")
    private String processDefinitionId;
    @ApiModelProperty("流程定义KEY")
    private String processDefinitionKey;
    @ApiModelProperty("流程部署ID")
    private String deploymentId;
}