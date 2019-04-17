package com.github.missthee.db.entity.primary.manage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author WORK,MT
 * @since 2019-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "可用")
    @TableField("is_enable")
    private Boolean isEnable;

    @ApiModelProperty(value = "已删除")
    @TableField("is_delete")
    private Boolean isDelete;

    @ApiModelProperty(value = "管理员账号")
    @TableField("is_admin")
    private Boolean isAdmin;

    @ApiModelProperty(value = "基础账号")
    @TableField("is_basic")
    private Boolean isBasic;


    public static final String ID = "id";

    public static final String NICKNAME = "nickname";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String IS_ENABLE = "is_enable";

    public static final String IS_DELETE = "is_delete";

    public static final String IS_ADMIN = "is_admin";

    public static final String IS_BASIC = "is_basic";

}
