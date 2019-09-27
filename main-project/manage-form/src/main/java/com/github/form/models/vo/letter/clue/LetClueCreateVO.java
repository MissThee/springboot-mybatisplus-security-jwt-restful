package com.github.form.models.vo.letter.clue;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class LetClueCreateVO {
    @NotNull
    private LocalDate receptionTime;
    @NotNull
    private String content;
    @NotNull
    private List<Integer> dicSourceIdList;
    private List<letDefendantListVO> letDefendantList;
    @NotNull
    private List<Integer> dicIllegalBehaviorIdList;
    @NotNull
    private List<Integer> dicAreaInvolvedIdList;
    private List<LetFileVO> fileList;

    @Data
    public static class letDefendantListVO {
        private String name;
        private String companyName;
        private String postName;
        private Integer jobRankId;
        private List<Integer> jobTypeIdList;
    }

    @Data
    public static class LetFileVO {
        private String name;
        private String file;
        private String url;
    }
}