package com.github.missthee.db.dto.manage.permissioncontroller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class SelectTreeReq {
    @ApiModelProperty(value = "排序<字段名,是正序>")
    private LinkedHashMap<String, Boolean> orderBy;
    @ApiModelProperty(value = "根节点id。不传此值或为null时，返回所有节点")
    private Long rootId;
    @ApiModelProperty(value = "是否包含已删除节点")
    private Boolean isDelete = false;
}
