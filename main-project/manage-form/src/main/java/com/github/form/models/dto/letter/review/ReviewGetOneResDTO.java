package com.github.form.models.dto.letter.review;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ReviewGetOneResDTO {
    private Long id;
    private ReviewClueOneDTO form;
    private List<String> canEditKeyList;
}