package com.github.missthee.db.dto.manage.permissioncontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SelectOneReq {
    @ApiModelProperty(value = "权限id")
    private Long id;
}
