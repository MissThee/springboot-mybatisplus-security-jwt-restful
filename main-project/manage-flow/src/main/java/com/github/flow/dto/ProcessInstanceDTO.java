package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProcessInstanceDTO {
    @ApiModelProperty("流程实例ID")
    private String getId;
    @ApiModelProperty("流程实例名称")
    private String getName;
    @ApiModelProperty("流程实例对应业务id")
    private String getBusinessKey;
    @ApiModelProperty("流程实例开始时间")
    private String getStartTime;
}