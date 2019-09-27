package com.github.form.models.vo.letter.dictionary;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("DicIllegalBehaviorVO.InsertOneRes")
public  class DicCreateOneResVO {
    @ApiModelProperty(value = "新增违法行为的id")
    private Integer id;
}
