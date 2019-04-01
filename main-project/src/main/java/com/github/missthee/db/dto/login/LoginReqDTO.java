package com.github.missthee.db.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "接收登录参数")
public class LoginReqDTO {
    @ApiModelProperty(value = "账号", required = true, example = "admin")
    private String username;
    @ApiModelProperty(value = "密码", required = true, example = "123")
    private String password;
    @ApiModelProperty(value = "token有效时长。true-7天；false-2天", required = true, example = "123")
    private Boolean isLongLogin = false;

}
