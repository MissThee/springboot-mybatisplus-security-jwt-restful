package com.github.base.dto.manage.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SysRoleInsertOneDTO {
    @ApiModelProperty(value = "角色名称", example = "测试角色1")
    private String name;
    @ApiModelProperty(value = "角色值（唯一）", example = "roleValue1")
    private String role;
    @ApiModelProperty(value = "可用", example = "true")
    private Boolean isEnable;
    @ApiModelProperty(value = "拥有的权限id集合")
    private List<Long> permissionIdList;
}
