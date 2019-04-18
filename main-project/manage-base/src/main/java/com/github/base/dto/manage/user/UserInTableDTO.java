package com.github.base.dto.manage.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserInTableDTO {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "可用")
    private Boolean isEnable;

    @ApiModelProperty(value = "拥有的角色名集合")
    private List<RoleInfo> roleList;

    @ApiModelProperty(value = "所属组织机构名称")
    private String unitName;

    @Data
    public static class RoleInfo {
        private String name;
        private Boolean isEnable;
    }
}
