package com.github.missthee.vo.manage;

import com.github.missthee.db.entity.primary.manage.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

public class UserControllerVO {
    @Data
    @ApiModel("UserControllerVO.DeleteOneReq")
    public static class DeleteOneReq {
        private Long id;
    }

    @Data
    @ApiModel("UserControllerVO.InsertOneReq")
    public static class InsertOneReq {
        private String nickname;
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @ApiModel("UserControllerVO.InsertOneRes")
    public static class InsertOneRes {
        private Long id;
    }

    @Data
    @ApiModel("UserControllerVO.SelectListReq")
    public static class SelectListReq {
        private LinkedHashMap<String, String> orderBy;
        private String username;
        private String nickname;
    }
    @Data
    @AllArgsConstructor
    @ApiModel("UserControllerVO.SelectListRes")
    public static class SelectListRes {
        private List<User> userList;
    }

    @Data
    @ApiModel("UserControllerVO.SelectOneReq")
    public class SelectOneReq {
        private Long id;
    }

    @Data
    @AllArgsConstructor
    @ApiModel("UserControllerVO.SelectOneRes")
    public static class SelectOneRes {
        private User user;
    }

    @Data
    @ApiModel("UserControllerVO.UpdateOneReq")
    public static class UpdateOneReq {
        private Long id;
        private Long nickname;
        private Long password;
    }
}
