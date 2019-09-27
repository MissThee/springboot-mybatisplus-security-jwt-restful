package com.github.common.db.entity.primary;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 违法行为
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DicIllegalBehavior对象", description="违法行为")
public class DicIllegalBehavior implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("index_number")
    private Integer indexNumber;

    @TableField("is_delete")
    private Boolean isDelete;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String INDEX_NUMBER = "index_number";

    public static final String IS_DELETE = "is_delete";

}
