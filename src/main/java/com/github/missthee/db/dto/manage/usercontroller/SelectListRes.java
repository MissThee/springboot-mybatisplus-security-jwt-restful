package com.github.missthee.db.dto.manage.usercontroller;

import com.github.missthee.db.po.primary.manage.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SelectListRes {
    private List<User> userList;
}