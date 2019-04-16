package com.github.missthee.db.vo.manage;

import com.github.missthee.db.dto.manage.user.UserInTableDTO;
import com.github.missthee.db.dto.manage.user.UserInTableDetailDTO;
import com.github.missthee.db.dto.manage.user.UserInsertOneDTO;
import com.github.missthee.db.dto.manage.user.UserUpdateOneDTO;
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
    public static class InsertOneReq extends UserInsertOneDTO {

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
        @ApiModelProperty(value = "排序条件<列名,是正序>")
        private LinkedHashMap<String, Boolean> orderBy;
        @ApiModelProperty(value = "true查看已删用户，false查看未删用户")
        private Boolean isDelete = false;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.SelectListRes")
    public static class SelectListRes {
        @ApiModelProperty(value = "用户列表")
        private List<UserInTableDTO> userInTableBoList;
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
    @ApiModel("UserVO.SelectOneReq")
    public static class SelectOneRes {
        @ApiModelProperty(value = "用户对象")
        private UserInTableDetailDTO user;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    @ApiModel("UserVO.UpdateOneReq")
    public static class UpdateOneReq extends UserUpdateOneDTO {

    }
}
