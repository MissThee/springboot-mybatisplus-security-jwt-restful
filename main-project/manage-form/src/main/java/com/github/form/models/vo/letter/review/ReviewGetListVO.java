package com.github.form.models.vo.letter.review;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class ReviewGetListVO {
    @NotNull
    private Integer pageIndex;
    @NotNull
    private Integer pageSize;
    @NotNull
    @Range(min = 0, max = 2, message = "type 值仅可为 0,1,2")
    private Integer isFinished;
}