package com.github.common.db.entity.primary;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 信访表单和主要违法行为关联表
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LetClueIllegalBehavior对象", description="信访表单和主要违法行为关联表")
public class LetClueIllegalBehavior implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("clue_id")
    private String clueId;

    @TableField("illegal_behavior_id")
    private Integer illegalBehaviorId;


    public static final String CLUE_ID = "clue_id";

    public static final String ILLEGAL_BEHAVIOR_ID = "illegal_behavior_id";

}
