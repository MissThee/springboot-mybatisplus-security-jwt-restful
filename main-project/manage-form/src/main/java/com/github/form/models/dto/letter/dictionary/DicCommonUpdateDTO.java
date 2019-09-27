package com.github.form.models.dto.letter.dictionary;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DicCommonUpdateDTO {
    private Integer id;
    private String name;
    private Integer indexNumber;
}
