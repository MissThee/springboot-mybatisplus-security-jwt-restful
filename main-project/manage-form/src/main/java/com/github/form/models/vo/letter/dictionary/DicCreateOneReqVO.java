package com.github.form.models.vo.letter.dictionary;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("DicIllegalBehaviorVO.InsertOneReq")
public  class DicCreateOneReqVO {
    private String name;
    private Integer indexNumber;
}
