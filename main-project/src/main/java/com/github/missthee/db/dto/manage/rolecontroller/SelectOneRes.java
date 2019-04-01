package com.github.missthee.db.dto.manage.rolecontroller;

import com.github.missthee.db.po.primary.manage.Role;
import com.github.missthee.db.po.primary.manage.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectOneRes {
    @ApiModelProperty(value = "角色对象")
    private Role role;
}