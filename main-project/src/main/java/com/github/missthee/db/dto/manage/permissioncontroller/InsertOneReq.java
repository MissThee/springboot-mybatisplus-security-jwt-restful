package com.github.missthee.db.dto.manage.permissioncontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class InsertOneReq {
    @ApiModelProperty(value = "父id")
    private Long parentId;
    @ApiModelProperty(value = "权限名称")
    private String name;
    @ApiModelProperty(value = "权限值")
    private String permission;
    @ApiModelProperty(value = "类型")
    private String Type;
    @ApiModelProperty(value = "可用")
    private String isEnable;

}