package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProcessDefinitionDTO {
    @ApiModelProperty("部署ID")
    private String getId;
    @ApiModelProperty("部署名称")
    private String getName;
    @ApiModelProperty("部署KEY")
    private String getKey;
    @ApiModelProperty("部署版本")
    private int getVersion;
}