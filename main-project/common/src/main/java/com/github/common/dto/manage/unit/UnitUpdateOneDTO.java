package com.github.common.dto.manage.unit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UnitUpdateOneDTO extends UnitInsertOneDTO {
    @ApiModelProperty(value = "组织结构id")
    private Long id;
}
