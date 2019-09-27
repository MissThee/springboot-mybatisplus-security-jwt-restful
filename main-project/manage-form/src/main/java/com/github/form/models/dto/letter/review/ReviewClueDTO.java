package com.github.form.models.dto.letter.review;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ReviewClueDTO {
    private String id;
    private String letClueId;
    private String title;
    private String stepName;
    private Boolean isFinished;
    private LocalDateTime createDate;
}