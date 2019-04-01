package com.github.missthee.db.dto.manage.rolecontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class InsertOneReq {
    @ApiModelProperty(value = "角色名称")
    private String name;
    @ApiModelProperty(value = "角色值（唯一）")
    private String role;
    @ApiModelProperty(value = "可用")
    private Boolean isEnable;
    @ApiModelProperty(value = "拥有的权限id集合")
    private List<Long> permissionIdList;
}