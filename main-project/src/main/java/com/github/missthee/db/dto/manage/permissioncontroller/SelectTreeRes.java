package com.github.missthee.db.dto.manage.permissioncontroller;

import com.alibaba.fastjson.JSONArray;
import com.github.missthee.db.po.primary.manage.Permission;
import com.github.missthee.db.po.primary.manage.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SelectTreeRes {
    @ApiModelProperty(value = "权限树")
    private List<Object> permissionTree;
}