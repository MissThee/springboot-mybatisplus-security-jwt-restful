package com.github.missthee.db.dto.manage.usercontroller;

import com.github.missthee.db.po.primary.manage.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectOneRes {
    private User user;
}