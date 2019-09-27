package com.github.form.models.vo.letter.review;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReviewCreateVO {
    @NotNull
    String letClueId;
}