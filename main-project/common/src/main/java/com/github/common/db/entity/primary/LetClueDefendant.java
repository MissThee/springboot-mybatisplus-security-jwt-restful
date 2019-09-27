package com.github.common.db.entity.primary;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 信访表单和被反映人关联表
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LetClueDefendant对象", description="信访表单和被反映人关联表")
public class LetClueDefendant implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("clue_id")
    private String clueId;

    @TableField("defendant_id")
    private String defendantId;


    public static final String CLUE_ID = "clue_id";

    public static final String DEFENDANT_ID = "defendant_id";

}
