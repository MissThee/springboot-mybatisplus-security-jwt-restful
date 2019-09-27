package com.github.common.db.entity.primary;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author WORK-PC,MT
 * @since 2019-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysSignature对象", description="")
public class SysSignature implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "签名文件路径")
    @TableField("sign_file_path")
    private String signFilePath;

    @ApiModelProperty(value = "创建时间（自动填充）")
    @TableField("create_date")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "是否作为默认签名（默认否）")
    @TableField("is_default")
    private Boolean isDefault;


    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String SIGN_FILE_PATH = "sign_file_path";

    public static final String CREATE_DATE = "create_date";

    public static final String IS_DEFAULT = "is_default";

}
