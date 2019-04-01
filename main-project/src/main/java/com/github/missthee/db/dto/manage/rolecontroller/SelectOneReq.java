package com.github.missthee.db.dto.manage.rolecontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SelectOneReq {
    @ApiModelProperty(value = "角色id")
    private Long id;
}
