package com.github.missthee.db.dto.manage.usercontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class SelectListReq {
    @ApiModelProperty(value = "排序条件<列名,是正序>")
    private LinkedHashMap<String, Boolean> orderBy;
    @ApiModelProperty(value = "是否仅有已删用户")
    private Boolean isDelete = false;
}
