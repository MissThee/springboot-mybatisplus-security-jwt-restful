package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProcessDefinitionDTO {
    @ApiModelProperty("部署ID")
    private String id;
    @ApiModelProperty("部署名称")
    private String name;
    @ApiModelProperty("部署KEY")
    private String key;
    @ApiModelProperty("部署版本")
    private Integer version;
    @ApiModelProperty("是否被挂起。被挂起的对象不能再进行流程操作")
    private Boolean isSuspended;
}