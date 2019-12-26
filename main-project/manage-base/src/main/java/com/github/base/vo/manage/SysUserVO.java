package com.github.base.vo.manage;

import com.github.base.dto.manage.user.SysUserInTableDTO;
import com.github.base.dto.manage.user.SysUserInTableDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;

public class SysUserVO {

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.DeleteOneReq")
    public static class DeleteOneReq {
        @ApiModelProperty(value = "id", example = "0")
        private Long id;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.InsertOneReq")
    public static class InsertOneReq  {
        @ApiModelProperty(value = "昵称", example = "用户1")
        @NotEmpty(message = "昵称不能为空")
        private String nickname;
        @ApiModelProperty(value = "用户名/账号", example = "user1")
        @NotEmpty(message = "用户名/账号不能为空")
        private String username;
        @ApiModelProperty(value = "初始密码", example = "123456")
        @NotEmpty(message = "初始密码不能为空")
        private String password;
        @ApiModelProperty(value = "账户状态true可用，false停用", example = "true")
        @NotNull(message = "账户状态不能为空")
        private Boolean isEnable;
        @ApiModelProperty(value = "是否为管理员", example = "false")
        @NotNull(message = "是否为管理员状态不能为空")
        private Boolean isAdmin;
        @ApiModelProperty(value = "角色id号集合")
        private List<Long> roleIdList;
        @ApiModelProperty(value = "所属组织机构id号")
        @NotNull(message = "所属组织机构不能为空")
        private Long unitId;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.InsertOneRes")
    public static class InsertOneRes {
        @ApiModelProperty(value = "新增用户的id")
        private Long id;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.SelectListReq")
    public static class SelectListReq {
        @ApiModelProperty(value = "页号")
        private Integer pageNum;
        @ApiModelProperty(value = "每页条数")
        private Integer pageSize;
        @ApiModelProperty(value = "排序条件<列名,是正序>")
        private LinkedHashMap<String, Boolean> orderBy;
        @ApiModelProperty(value = "true查看已删用户，false查看未删用户",example = "false")
        private Boolean isDelete = false;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.SelectListRes")
    public static class SelectListRes {
        @ApiModelProperty(value = "用户列表")
        private List<SysUserInTableDTO> userList;
        @ApiModelProperty(value = "总条数")
        private Long total;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.SelectOneReq")
    public static class SelectOneReq {
        @ApiModelProperty(value = "用户id")
        private Long id;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.SelectOneRes")
    public static class SelectOneRes {
        @ApiModelProperty(value = "用户对象")
        private SysUserInTableDetailDTO user;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.UpdateOneReq")
    public static class UpdateOneReq {
        @ApiModelProperty(value = "用户id")
        private Long id;
        @ApiModelProperty(value = "昵称", example = "用户1")
        @NotEmpty(message = "昵称不能为空")
        private String nickname;
        @ApiModelProperty(value = "用户名/账号", example = "user1")
        @NotEmpty(message = "用户名/账号不能为空")
        private String username;
        @ApiModelProperty(value = "账户状态", example = "true")
        @NotNull(message = "账户状态不能为空")
        private Boolean isEnable;
        @ApiModelProperty(value = "是否为管理员", example = "false")
        @NotNull(message = "是否为管理员状态不能为空")
        private Boolean isAdmin;
        @ApiModelProperty(value = "角色id号集合")
        private List<Long> roleIdList;
        @ApiModelProperty(value = "所属组织机构id号")
        @NotNull(message = "所属组织机构不能为空")
        private Long unitId;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.ResetDefaultPasswordReq")
    public static class ResetDefaultPasswordReq {
        @ApiModelProperty(value = "用户id")
        private Long id;
    }
}
