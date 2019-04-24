package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class HistoricVariableInstanceDTO {
    @ApiModelProperty("历史参数ID")
    private String id;
    @ApiModelProperty("历史参数名称")
    private String variableName;
    @ApiModelProperty("历史参数值")
    private String value;
    @ApiModelProperty("历史参数创建时间")
    private Date createTime;
    @ApiModelProperty("历史参数流程实例ID")
    private String processInstanceId;
    @ApiModelProperty("历史参数任务ID")
    private String taskId;
}