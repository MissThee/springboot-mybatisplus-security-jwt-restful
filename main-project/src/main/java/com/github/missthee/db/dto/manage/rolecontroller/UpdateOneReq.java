package com.github.missthee.db.dto.manage.rolecontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateOneReq extends InsertOneReq {
    @ApiModelProperty(value = "角色id")
    private Long id;
}