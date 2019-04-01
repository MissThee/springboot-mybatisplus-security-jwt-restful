package com.github.missthee.db.dto.login;

import com.github.missthee.db.bo.login.LoginBO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginResDTO {
    @ApiModelProperty(value = "用户对象")
    private LoginBO user;
}