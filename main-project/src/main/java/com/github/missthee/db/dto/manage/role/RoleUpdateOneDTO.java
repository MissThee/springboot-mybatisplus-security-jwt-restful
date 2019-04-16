package com.github.missthee.db.dto.manage.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RoleUpdateOneDTO extends RoleInsertOneDTO {
    @ApiModelProperty(value = "角色id")
    private Long id;
}
