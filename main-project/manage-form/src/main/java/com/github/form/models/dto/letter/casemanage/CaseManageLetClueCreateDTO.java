package com.github.form.models.dto.letter.casemanage;

import com.github.form.models.dto.letter.clue.LetClueCreateDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CaseManageLetClueCreateDTO extends LetClueCreateDTO {
    private Integer resultTypeId;
}
