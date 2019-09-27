package com.github.form.models.vo.letter.clue;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LetClueDeleteVO {
    @NotNull
    private String id;
}