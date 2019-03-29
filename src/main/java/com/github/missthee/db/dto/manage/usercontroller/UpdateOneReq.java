package com.github.missthee.db.dto.manage.usercontroller;

import lombok.Data;

@Data
public class UpdateOneReq {
    private Long id;
    private Long nickname;
    private Long password;
}