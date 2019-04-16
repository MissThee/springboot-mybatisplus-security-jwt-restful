package com.github.missthee.db.vo.manage;

import com.github.missthee.db.dto.manage.role.RoleInsertOneDTO;
import com.github.missthee.db.dto.manage.role.RoleUpdateOneDTO;
import com.github.missthee.db.entity.primary.manage.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.List;

public class RoleVO {

    @Data
    @Accessors(chain = true)
    public static class DeleteOneReq {
        @ApiModelProperty(value = "id", example = "0")
        private Long id;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    public static class InsertOneReq extends RoleInsertOneDTO {

    }

    @Data
    @Accessors(chain = true)
    public static class InsertOneRes {
        @ApiModelProperty(value = "新增角色的id")
        private Long id;
    }

    @Data
    @Accessors(chain = true)
    public static class SelectListReq {
        @ApiModelProperty(value = "排序<字段名,是正序>", example = "{'name':true}")
        private LinkedHashMap<String, Boolean> orderBy;
        @ApiModelProperty(value = "true查看已删角色，false查看未删角色", example = "false")
        private Boolean isDelete = false;
    }

    @Data
    @Accessors(chain = true)
    public static class SelectListRes {
        @ApiModelProperty(value = "角色列表")
        private List<Role> roleList;
    }

    @Data
    @Accessors(chain = true)
    public static class SelectOneReq {
        @ApiModelProperty(value = "角色id")
        private Long id;
    }

    @Data
    @Accessors(chain = true)
    public static class SelectOneRes {
        @ApiModelProperty(value = "角色对象")
        private Role role;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    public static class UpdateOneReq extends RoleUpdateOneDTO {

    }
}
