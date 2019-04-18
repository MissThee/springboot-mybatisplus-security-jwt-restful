package com.github.base.dto.manage.myaccount;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MyAccountUpdatePasswordDTO {
    @ApiModelProperty(value = "用户id", example = "0")
    private Long id;
    @ApiModelProperty(value = "新密码", example = "654321")
    private String newPassword;
}
