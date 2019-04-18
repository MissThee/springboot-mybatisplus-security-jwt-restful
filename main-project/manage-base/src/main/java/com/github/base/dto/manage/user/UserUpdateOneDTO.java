package com.github.base.dto.manage.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserUpdateOneDTO  {
    @ApiModelProperty(value = "用户id")
    private Long id;
    @ApiModelProperty(value = "昵称", example = "用户1")
    private String nickname;
    @ApiModelProperty(value = "用户名", example = "testuser1")
    private String username;
    @ApiModelProperty(value = "账户状态", example = "true")
    private Boolean isEnable;
    @ApiModelProperty(value = "角色id号集合")
    private List<Long> roleIdList;
    @ApiModelProperty(value = "组织结构id号")
    private Long unitId;
}
