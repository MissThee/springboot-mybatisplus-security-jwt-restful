package com.github.missthee.db.dto.manage.usercontroller;

import com.github.missthee.db.entity.primary.manage.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SelectListRes {
    @ApiModelProperty(value = "用户列表")
    private List<User> userList;
}