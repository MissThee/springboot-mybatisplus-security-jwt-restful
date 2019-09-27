package com.github.form.models.dto.letter.clue;

import com.github.form.models.dto.LetFileResDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class LetClueCreateDTO {
    private String id;
    private LocalDate receptionTime;
    private String content;
    private Integer resultTypeId;
    private List<Integer> dicSourceIdList;
    private List<LetDefendantDTO> letDefendantList;
    private List<Integer> dicIllegalBehaviorIdList;
    private List<Integer> dicAreaInvolvedIdList;
    private List<LetFileResDTO> fileList;
    private Long creatorId;

    @Data
    public static class LetDefendantDTO {
        private String id;
        private String name;
        private String companyName;
        private String postName;
        private Integer jobRankId;
        private List<Integer> jobTypeIdList;
    }


}


