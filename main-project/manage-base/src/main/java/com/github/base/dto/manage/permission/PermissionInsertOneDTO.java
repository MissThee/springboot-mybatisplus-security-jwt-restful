package com.github.base.dto.manage.permission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PermissionInsertOneDTO {
    @ApiModelProperty(value = "父id", example = "0")
    private Long parentId;
    @ApiModelProperty(value = "权限名称", example = "测试权限1")
    private String name;
    @ApiModelProperty(value = "权限值", example = "testPermission1")
    private String permission;
    @ApiModelProperty(value = "类型", example = "button")
    private String Type;
    @ApiModelProperty(value = "可用", example = "true")
    private String isEnable;
}