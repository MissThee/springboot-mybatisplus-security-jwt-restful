package com.github.form.models.dto.letter.review;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;

@Data
@Accessors(chain = true)
public class ReviewGetListResDTO {
    private Long total;
    private Collection<ReviewClueDTO> formList;
}