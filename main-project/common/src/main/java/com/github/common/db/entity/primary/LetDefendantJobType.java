package com.github.common.db.entity.primary;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 被反映人和是否村干关联表
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LetDefendantJobType对象", description="被反映人和是否村干关联表")
public class LetDefendantJobType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("defendant_id")
    private String defendantId;

    @TableField("job_type_id")
    private Integer jobTypeId;

    public static final String DEFENDANT_ID = "defendant_id";

    public static final String JOB_TYPE_ID = "job_type_id";

}
