package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProcessInstanceDTO {
    @ApiModelProperty("流程实例ID")
    private String id;
    @ApiModelProperty("流程实例名称")
    private String name;
    @ApiModelProperty("流程实例对应业务id")
    private String businessKey;
    @ApiModelProperty("流程实例开始时间")
    private Date startTime;
}