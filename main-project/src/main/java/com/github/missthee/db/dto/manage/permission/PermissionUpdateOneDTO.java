package com.github.missthee.db.dto.manage.permission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PermissionUpdateOneDTO extends PermissionInsertOneDTO {
    @ApiModelProperty(value = "权限id")
    private Long id;
}