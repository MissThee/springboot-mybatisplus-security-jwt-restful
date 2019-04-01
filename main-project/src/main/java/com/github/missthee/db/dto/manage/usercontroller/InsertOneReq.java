package com.github.missthee.db.dto.manage.usercontroller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class InsertOneReq {
    @ApiModelProperty(value = "昵称", example = "0")
    private String nickname;
    @ApiModelProperty(value = "用户名", example = "0")
    private String username;
    @ApiModelProperty(value = "密码", example = "0")
    private String password;
    @ApiModelProperty(value = "账户状态", example = "true")
    private Boolean isEnable;
    @ApiModelProperty(value = "角色id号集合")
    private List<Long> roleIdList;
}