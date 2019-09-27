package com.github.form.models.vo.letter.casemanage;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CaseManageRefuseVO {
    @NotNull
    private Long id;
}
