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

/**
 * <p>
 * 线索来源
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DicSource对象", description="线索来源")
public class DicSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "线索来源id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "线索来源名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "排序")
    @TableField("index_number")
    private Integer indexNumber;

    @ApiModelProperty(value = "已删除")
    @TableField("is_delete")
    private Boolean isDelete;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String INDEX_NUMBER = "index_number";

    public static final String IS_DELETE = "is_delete";

}
