package com.github.form.models.vo.letter.clue;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class LetClueGetListVO {
    @NotNull
    private Integer pageIndex;
    @NotNull
    private Integer pageSize;
    private String resultTypeId;
    private LocalDate startReceptionTime;
    private LocalDate endReceptionTime;
    private String content;
    private String defendantName;
}