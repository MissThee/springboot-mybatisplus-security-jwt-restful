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
}