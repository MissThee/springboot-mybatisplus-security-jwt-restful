package com.github.form.models.vo.letter.review;

import lombok.Data;

import java.util.Map;

@Data
public  class ReviewUpdateVO {
    Long id;
    Map<String, String> form;
}