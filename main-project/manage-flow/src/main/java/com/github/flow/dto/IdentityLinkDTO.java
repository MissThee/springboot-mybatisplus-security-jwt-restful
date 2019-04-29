package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IdentityLinkDTO {
    @ApiModelProperty("人员ID")
    private String userId;
}