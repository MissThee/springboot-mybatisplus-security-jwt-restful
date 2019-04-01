package com.github.missthee.db.dto.manage.permissioncontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateOneReq extends InsertOneReq{
    @ApiModelProperty(value = "权限id")
    private Long id;
}