package com.github.common.db.entity.primary;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 信访表单和线索来源关联表
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LetClueSource对象", description="信访表单和线索来源关联表")
public class LetClueSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "线索id")
    @TableField("clue_id")
    private String clueId;

    @ApiModelProperty(value = "来源id")
    @TableField("source_id")
    private Integer sourceId;


    public static final String CLUE_ID = "clue_id";

    public static final String SOURCE_ID = "source_id";

}
