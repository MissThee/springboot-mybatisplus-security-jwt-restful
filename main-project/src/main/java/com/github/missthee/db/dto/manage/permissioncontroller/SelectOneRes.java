package com.github.missthee.db.dto.manage.permissioncontroller;

import com.github.missthee.db.entity.primary.manage.Permission;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectOneRes {
    @ApiModelProperty(value = "权限对象")
    private Permission permission;
}