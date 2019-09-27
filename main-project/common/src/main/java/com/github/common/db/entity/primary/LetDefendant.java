package com.github.common.db.entity.primary;

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
 * 被反映人
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LetDefendant对象", description="被反映人")
public class LetDefendant implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @ApiModelProperty(value = "姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "工作单位名称")
    @TableField("company_name")
    private String companyName;

    @ApiModelProperty(value = "职务")
    @TableField("post_name")
    private String postName;

    @ApiModelProperty(value = "职级")
    @TableField("job_rank_id")
    private Integer jobRankId;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String COMPANY_NAME = "company_name";

    public static final String POST_NAME = "post_name";

    public static final String JOB_RANK_ID = "job_rank_id";

}
