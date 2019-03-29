package com.github.missthee.db.dto.manage.usercontroller;

import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class SelectListReq {
    private LinkedHashMap<String, String> orderBy;
    private String username;
    private String nickname;
}
