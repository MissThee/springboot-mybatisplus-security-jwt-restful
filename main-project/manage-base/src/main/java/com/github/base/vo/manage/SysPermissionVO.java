package com.github.base.vo.manage;

import com.github.common.db.entity.primary.SysPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;

public class SysPermissionVO {
    @Data
    @Accessors(chain = true)
    @ApiModel("PermissionVO.DeleteOneReq")
    public static class DeleteOneReq {
        @ApiModelProperty(value = "id", example = "0")
        private Long id;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("PermissionVO.InsertOneReq")
    public static class InsertOneReq   {
        @ApiModelProperty(value = "父级id")
        private Long parentId;
        @ApiModelProperty(value = "名称")
        @NotEmpty(message = "名称不能为空")
        private String name;
        @ApiModelProperty(value = "权限值（唯一），应取自后台编码设定的权限值")
        @NotEmpty(message = "权限值不能为空")
        private String permission;
        @ApiModelProperty(value = "类型")
        private String Type;
        @ApiModelProperty(value = "可用状态")
        private String isEnable;
        @ApiModelProperty(value = "排序序号")
        private Integer indexNum;
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
    public static class UpdateOneReq extends InsertOneReq {
        @ApiModelProperty(value = "权限id")
        private Long id;
    }
}
