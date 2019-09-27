package com.github.form.models.dto.letter.casemanage;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CaseManageListResDTO {

    private Integer id;
    private String content;
    private Boolean isNeedResultForm;
    private LocalDateTime resultFormLimitDate;
    private String sendToSubDeptName;
    private Integer stateId;
    private LocalDateTime operationDate;
    private LetClueListResDTO letClue;

    @Data
    public static class LetClueListResDTO {
        private String id;
        private LocalDate receptionTime;
        private String content;
        private List<LetDefendantListResDTO> letDefendantList;
    }

    @Data
    public static class LetDefendantListResDTO {
        private String id;
        private String name;
        private String companyName;
    }
}

