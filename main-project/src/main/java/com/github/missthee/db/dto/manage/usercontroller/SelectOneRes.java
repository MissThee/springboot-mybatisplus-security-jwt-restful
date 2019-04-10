package com.github.missthee.db.dto.manage.usercontroller;

import com.github.missthee.db.entity.primary.manage.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectOneRes {
    @ApiModelProperty(value = "用户对象")
    private User user;
}