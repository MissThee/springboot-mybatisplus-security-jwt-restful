package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FlowLinePositionDTO {
    @ApiModelProperty("x坐标")
    private String x;
    @ApiModelProperty("y坐标")
    private String y;
}