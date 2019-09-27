package com.github.form.models.dto.letter.clue;


import lombok.Data;

import java.util.List;

/**
 * Created by william on 2019/07/04
 * Description:
 **/
@Data
public class LetDefendantResDTO {
    private String id;

    private String name;

    private String companyName;

    private String postName;

    private Integer jobRankId;

    private List<Integer> jobTypeIdList;
}
