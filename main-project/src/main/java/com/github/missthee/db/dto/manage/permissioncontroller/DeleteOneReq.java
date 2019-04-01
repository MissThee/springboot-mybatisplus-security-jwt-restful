package com.github.missthee.db.dto.manage.permissioncontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeleteOneReq {
    @ApiModelProperty(value = "id", example = "0")
    private Long id;
}