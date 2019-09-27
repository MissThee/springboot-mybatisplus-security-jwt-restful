package com.github.form.models.vo.letter.clue;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class LetClueUpdateVO extends LetClueCreateVO {
    @NotNull
    private String id;
}