package com.github.form.models.vo.letter.clue;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LetClueUpdateResultTypeVO {
    @NotNull
    private String id;
    @NotNull
    private Integer resultTypeId;
}