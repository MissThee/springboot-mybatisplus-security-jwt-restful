package com.github.form.models.dto.letter.casemanage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CaseManageResDTO {
    private Long id;

    @ApiModelProperty(value = "意见")
    private String content;

    @ApiModelProperty(value = "是否要结果件")
    private Boolean isNeedResultForm;

    @ApiModelProperty(value = "要结果件时，限制的时间")
    private LocalDateTime resultFormLimitDate;

    @ApiModelProperty(value = "分派给案管室的1-15编号")
    private Integer sendToSubDeptId;

    @ApiModelProperty(value = "办理状态：0-默认未办理，1-已办理，2-已驳回")
    private Integer stateId;

}
