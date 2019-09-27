package com.github.base.vo.manage;

import com.github.base.dto.manage.permission.SysPermissionInsertOneDTO;
import com.github.base.dto.manage.permission.SysPermissionUpdateOneDTO;
import com.github.common.db.entity.primary.SysPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.List;

public class PermissionVO {
    @Data
    @Accessors(chain = true)
    @ApiModel("PermissionVO.DeleteOneReq")
    public static class DeleteOneReq {
        @ApiModelProperty(value = "id", example = "0")
        private Long id;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    @ApiModel("PermissionVO.InsertOneReq")
    public static class InsertOneReq extends SysPermissionInsertOneDTO {

    }

    @Data
    @Accessors(chain = true)
    @ApiModel("PermissionVO.InsertOneRes")
    public static class InsertOneRes {
        @ApiModelProperty(value = "新增的权限id")
        private Long id;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("PermissionVO.SelectOneReq")
    public static class SelectOneReq {
        @ApiModelProperty(value = "权限id")
        private Long id;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("PermissionVO.SelectOneRes")
    public static class SelectOneRes {
        @ApiModelProperty(value = "权限对象")
        private SysPermission permission;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("PermissionVO.SelectTreeReq")
    public static class SelectTreeReq {
        @ApiModelProperty(value = "排序<字段名,是正序>", example = "{'name':true}")
        private LinkedHashMap<String, Boolean> orderBy;
        @ApiModelProperty(value = "根节点id。不传此值或为null时，返回所有节点", example = "null")
        private Long rootId;
        @ApiModelProperty(value = "是否包含已删除节点", example = "false")
        private Boolean isDelete = false;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("PermissionVO.SelectTreeRes")
    public static class SelectTreeRes {
        @ApiModelProperty(value = "权限树")
        private List<Object> permissionTree;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    @ApiModel("PermissionVO.UpdateOneReq")
    public static class UpdateOneReq extends SysPermissionUpdateOneDTO {
        @ApiModelProperty(value = "权限id")
        private Long id;
    }
}
