package com.github.missthee.db.dto.manage.permissioncontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsertOneRes {
    @ApiModelProperty(value = "新增的权限id")
    private Long id;
}