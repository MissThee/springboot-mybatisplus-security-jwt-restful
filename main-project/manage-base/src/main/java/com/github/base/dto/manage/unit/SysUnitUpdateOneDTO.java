package com.github.base.dto.manage.unit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SysUnitUpdateOneDTO extends SysUnitInsertOneDTO {
    @ApiModelProperty(value = "组织结构id")
    private Long id;
}
