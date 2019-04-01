package com.github.missthee.db.dto.manage.rolecontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class SelectListReq {
    @ApiModelProperty(value = "排序<字段名,是正序>")
    private LinkedHashMap<String, Boolean> orderBy;
    @ApiModelProperty(value = "是否仅有已删角色")
    private Boolean isDelete = false;
}
