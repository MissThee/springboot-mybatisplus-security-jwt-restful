package com.github.form.models.dto.letter.clue;

import com.github.form.models.dto.LetFileResDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by william on 2019/07/04
 * Description:
 **/
@Data
public class LetClueResDTO {
    private String id;

    private LocalDate receptionTime;

    private String content;

    private List<Integer> dicSourceIdList;

    private List<LetDefendantResDTO> letDefendantList;

    private List<LetFileResDTO> fileList;

    private List<Integer> dicIllegalBehaviorIdList;

    private List<Integer> dicAreaInvolvedIdList;


}
