package com.github.common.db.entity.secondary;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author WORK-PC,MT
 * @since 2019-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TableInDb2对象", description="")
public class TableInDb2 implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("column1")
    private Integer column1;

    @TableField("column2")
    private Integer column2;


    public static final String ID = "id";

    public static final String COLUMN1 = "column1";

    public static final String COLUMN2 = "column2";

}
