package com.github.form.models.dto.letter.clue;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LetClueListResDTO {
    private String id;
    private LocalDate receptionTime;
    private String content;
    private LocalDateTime createDate;
    private Integer resultTypeId;
    private String resultTypeName;
    private Boolean isEvent;
    private List<LetDefendantListResDTO> letDefendantList;

    @Data
    public static class LetDefendantListResDTO {
        private String id;
        private String name;
        private String companyName;
    }
}
