package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProcessDefinitionDTO {
    @ApiModelProperty("流程定义ID")
    private String id;
    @ApiModelProperty("流程定义名称")
    private String name;
    @ApiModelProperty("流程定义KEY")
    private String key;
    @ApiModelProperty("流程定义版本")
    private Integer version;
    @ApiModelProperty("是否被挂起。被挂起的对象不能再进行流程操作")
    private Boolean isSuspended;
}