package com.github.common.db.entity.primary.manage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author mt
 * @since 2019-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Role对象", description="")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "角色值")
    @TableField("role")
    private String role;

    @ApiModelProperty(value = "可用")
    @TableField("is_enable")
    private Boolean isEnable;

    @ApiModelProperty(value = "已删除")
    @TableField("is_delete")
    private Boolean isDelete;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String ROLE = "role";

    public static final String IS_ENABLE = "is_enable";

    public static final String IS_DELETE = "is_delete";

}
