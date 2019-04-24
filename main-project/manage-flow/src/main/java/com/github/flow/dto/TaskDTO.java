package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TaskDTO {
    @ApiModelProperty("任务ID")
    private String Id;
    @ApiModelProperty("任务名称")
    private String Name;
    @ApiModelProperty("任务创建时间")
    private Date CreateTime;
    @ApiModelProperty("任务办理人")
    private String Assignee;
    @ApiModelProperty("执行实例ID")
    private String ExecutionId;
    @ApiModelProperty("流程实例ID")
    private String ProcessInstanceId;
    @ApiModelProperty("流程定义ID")
    private String ProcessDefinitionId;
}
