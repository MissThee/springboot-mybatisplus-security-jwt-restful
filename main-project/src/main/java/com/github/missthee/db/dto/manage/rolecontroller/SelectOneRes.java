package com.github.missthee.db.dto.manage.rolecontroller;

import com.github.missthee.db.entity.primary.manage.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectOneRes {
    @ApiModelProperty(value = "角色对象")
    private Role role;
}