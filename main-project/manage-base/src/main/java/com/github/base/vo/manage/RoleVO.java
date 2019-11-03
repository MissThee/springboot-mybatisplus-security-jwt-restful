package com.github.base.vo.manage;

import com.github.base.dto.manage.role.SysRoleInTableDetailDTO;
import com.github.base.dto.manage.role.SysRoleInsertOneDTO;
import com.github.base.dto.manage.role.SysRoleUpdateOneDTO;
import com.github.common.db.entity.primary.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.List;

public class RoleVO {

    @Data
    @Accessors(chain = true)
    @ApiModel("RoleVO.DeleteOneReq")
    public static class DeleteOneReq {
        @ApiModelProperty(value = "id", example = "0")
        private Long id;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    @ApiModel("RoleVO.RoleInsertOneDTO")
    public static class InsertOneReq extends SysRoleInsertOneDTO {

    }

    @Data
    @Accessors(chain = true)
    @ApiModel("RoleVO.InsertOneRes")
    public static class InsertOneRes {
        @ApiModelProperty(value = "新增角色的id")
        private Long id;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("RoleVO.SelectListReq")
    public static class SelectListReq {
        @ApiModelProperty(value = "页号")
        private Integer pageNum;
        @ApiModelProperty(value = "每页条数")
        private Integer pageSize;
        @ApiModelProperty(value = "排序<字段名,是正序>", example = "{'name':true}")
        private LinkedHashMap<String, Boolean> orderBy;
        @ApiModelProperty(value = "true查看已删角色，false查看未删角色", example = "false")
        private Boolean isDelete = false;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("RoleVO.SelectListRes")
    public static class SelectListRes {
        @ApiModelProperty(value = "角色列表")
        private List<SysRole> roleList;
        @ApiModelProperty(value = "总条数")
        private Long total;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("RoleVO.SelectOneReq")
    public static class SelectOneReq {
        @ApiModelProperty(value = "角色id")
        private Long id;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("RoleVO.SelectOneRes")
    public static class SelectOneRes {
        @ApiModelProperty(value = "角色对象")
        private SysRoleInTableDetailDTO role;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    @ApiModel("RoleVO.UpdateOneReq")
    public static class UpdateOneReq extends SysRoleUpdateOneDTO {

    }
}
