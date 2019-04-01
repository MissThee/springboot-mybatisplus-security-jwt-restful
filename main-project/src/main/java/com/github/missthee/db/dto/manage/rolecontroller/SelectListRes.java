package com.github.missthee.db.dto.manage.rolecontroller;

import com.github.missthee.db.po.primary.manage.Role;
import com.github.missthee.db.po.primary.manage.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SelectListRes {
    @ApiModelProperty(value = "角色列表")
    private List<Role> roleList;
}