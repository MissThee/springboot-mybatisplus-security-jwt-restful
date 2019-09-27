package com.github.form.models.vo.letter.dictionary;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("DicIllegalBehaviorVO.SelectListReq")
public  class DicGetListReqVO {
    @ApiModelProperty(value = "true查看已删违法行为，false查看未删违法行为，默认false", example = "false")
    private Boolean isDelete = false;
}