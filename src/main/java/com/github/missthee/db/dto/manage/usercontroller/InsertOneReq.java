package com.github.missthee.db.dto.manage.usercontroller;

import lombok.Data;

@Data
public class InsertOneReq {
    private String nickname;
    private String username;
    private String password;
}