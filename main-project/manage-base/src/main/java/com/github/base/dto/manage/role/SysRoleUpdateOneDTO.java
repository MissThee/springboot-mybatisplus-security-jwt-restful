package com.github.base.dto.manage.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SysRoleUpdateOneDTO extends SysRoleInsertOneDTO {
    @ApiModelProperty(value = "角色id")
    private Long id;
}
