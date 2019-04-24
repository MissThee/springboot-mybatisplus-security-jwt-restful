package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public  class FlowNodePositionDTO {
    @ApiModelProperty("x坐标")
    private String x;
    @ApiModelProperty("y坐标")
    private String y;
    @ApiModelProperty("宽")
    private String width;
    @ApiModelProperty("高")
    private String height;
}