package com.github.base.dto.manage.unit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UnitInsertOneDTO {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "父id")
    private Long parentId;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "类型")
    private String type;
}
