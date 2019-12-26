package com.github.base.vo.manage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

public class MyAccountVO {
    @Data
    @Accessors(chain = true)
    @ApiModel("MyAccountVO.UpdatePasswordReq")
    public static class UpdatePasswordReq {
        @ApiModelProperty("旧密码")
        @NotEmpty(message = "旧密码不能为空")
        private String oldPassword;
        @ApiModelProperty("新密码")
        @NotEmpty(message = "旧密码不能为空")
        private String newPassword;
    }
}
