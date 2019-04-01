package com.github.missthee.db.dto.manage.rolecontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsertOneRes {
    @ApiModelProperty(value = "新增角色的id")
    private Long id;
}