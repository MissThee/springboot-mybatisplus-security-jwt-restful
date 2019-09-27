package com.github.common.db.entity.primary;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 上传附件
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LetStuff对象", description="上传附件")
public class Stuff implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("relation_id")
    private String relationId;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("file")
    private String file;

    @TableField("relation_type")
    private Integer relationType;

    @TableField("create_date")
    private LocalDateTime createDate;

    public static final String RELATION_ID = "relation_id";

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String FILE = "file";

    public static final String RELATION_TYPE = "relation_type";

    public static final String CREATE_DATE = "create_date";

}
