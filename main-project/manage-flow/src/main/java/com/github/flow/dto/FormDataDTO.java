package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormDataDTO {
    @ApiModelProperty("属性id")
    private String id;
    @ApiModelProperty("属性名")
    private String name;
    @ApiModelProperty("属性值")
    private Object value;
    @ApiModelProperty("是否可写")
    private Boolean isWritable;
    @ApiModelProperty("是否必须")
    private Boolean isRequired;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("附加信息：type值为date时，此值为规定的时间格式；type值为enum时此值为可选值枚举。")
    private Object information;

    private FormDataDTO isWritable(Boolean isWritable) {
        this.isWritable = isWritable;
        return this;
    }

    private Boolean isWritable() {
        return this.isWritable;
    }
}