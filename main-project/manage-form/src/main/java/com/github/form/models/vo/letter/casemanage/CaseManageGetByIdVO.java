package com.github.form.models.vo.letter.casemanage;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CaseManageGetByIdVO {
    @NotNull
    private Long id;
}
