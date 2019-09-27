package com.github.form.models.vo.letter.casemanage;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CaseManageGetListVO {
    @NotNull
    private Integer pageIndex;
    @NotNull
    private Integer pageSize;

    private String letClueId;
    private String defendantName;
    private String content;
    private LocalDate startReceptionTime;
    private LocalDate endReceptionTime;
    private Integer leftDayNum;
}
