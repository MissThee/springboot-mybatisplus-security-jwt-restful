package com.github.base.vo.manage;

import com.github.base.dto.manage.user.SysUserInTableDTO;
import com.github.base.dto.manage.user.SysUserInTableDetailDTO;
import com.github.base.dto.manage.user.SysUserInsertOneDTO;
import com.github.base.dto.manage.user.SysUserUpdateOneDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.List;

public class UserVO {

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.DeleteOneReq")
    public static class DeleteOneReq {
        @ApiModelProperty(value = "id", example = "0")
        private Long id;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.InsertOneReq")
    public static class InsertOneReq extends SysUserInsertOneDTO {

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

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.UpdateOneReq")
    public static class UpdateOneReq extends SysUserUpdateOneDTO {

    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.ResetDefaultPasswordReq")
    public static class ResetDefaultPasswordReq {
        @ApiModelProperty(value = "用户id")
        private Long id;
    }
}
