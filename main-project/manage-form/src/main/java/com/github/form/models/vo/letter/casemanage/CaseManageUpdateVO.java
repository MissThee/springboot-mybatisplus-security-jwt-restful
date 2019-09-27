package com.github.form.models.vo.letter.casemanage;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Data
public class CaseManageUpdateVO {
    @NotNull
    private Long id;
    @NotNull
    @ApiModelProperty(value = "意见")
    private String content;
    @NotNull
    @ApiModelProperty(value = "是否要结果件")
    private Boolean isNeedResultForm;
    @ApiModelProperty(value = "要结果件时，限制的时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime resultFormLimitDate;
    @NotNull
    @ApiModelProperty(value = "分派给案管室的1-15编号")
    private Integer sendToSubDeptId;

}
