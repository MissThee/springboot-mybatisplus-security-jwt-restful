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
 * 问题线索处置方案呈批笺
 * </p>
 *
 * @author WORK-PC,MT
 * @since 2019-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ReviewClue对象", description = "问题线索处置方案呈批笺")
public class ReviewClue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "线索id")
    @TableField("let_clue_id")
    private String letClueId;

    @ApiModelProperty(value = "文件标题")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "本委领导批示")
    @TableField("bwld_content")
    private String bwldContent;

    @ApiModelProperty(value = "本委领导id")
    @TableField("bwld_id")
    private Integer bwldId;

    @ApiModelProperty(value = "本委领导姓名")
    @TableField("bwld_name")
    private String bwldName;

    @ApiModelProperty(value = "本委领导签字")
    @TableField("bwld_sign")
    private String bwldSign;

    @ApiModelProperty(value = "本委领导批示时间")
    @TableField("bwld_date")
    private LocalDateTime bwldDate;

    @ApiModelProperty(value = "分管副书记意见")
    @TableField("fgfsj_content")
    private String fgfsjContent;

    @ApiModelProperty(value = "分管副书记d")
    @TableField("fgfsj_id")
    private Integer fgfsjId;

    @ApiModelProperty(value = "分管副书记名字")
    @TableField("fgfsj_name")
    private String fgfsjName;

    @ApiModelProperty(value = "分管副书记签字")
    @TableField("fgfsj_sign")
    private String fgfsjSign;

    @ApiModelProperty(value = "分管副书记意见时间")
    @TableField("fgfsj_date")
    private LocalDateTime fgfsjDate;

    @ApiModelProperty(value = "主管常委意见")
    @TableField("zgcw_content")
    private String zgcwContent;

    @ApiModelProperty(value = "主管常委id")
    @TableField("zgcw_id")
    private Integer zgcwId;

    @ApiModelProperty(value = "主管常委名字")
    @TableField("zgcw_name")
    private String zgcwName;

    @ApiModelProperty(value = "主管常委签字")
    @TableField("zgcw_sign")
    private String zgcwSign;

    @ApiModelProperty(value = "主管常委意见时间")
    @TableField("zgcw_date")
    private LocalDateTime zgcwDate;

    @ApiModelProperty(value = "审查室意见")
    @TableField("scs_content")
    private String scsContent;

    @ApiModelProperty(value = "审查室id")
    @TableField("scs_id")
    private Integer scsId;

    @ApiModelProperty(value = "审查室名字")
    @TableField("scs_name")
    private String scsName;

    @ApiModelProperty(value = "审查室签字")
    @TableField("scs_sign")
    private String scsSign;

    @ApiModelProperty(value = "审查室意见时间")
    @TableField("scs_date")
    private LocalDateTime scsDate;

    @ApiModelProperty(value = "经办人")
    @TableField("operator")
    private String operator;

    @ApiModelProperty(value = "联系电话")
    @TableField("operator_phone")
    private String operatorPhone;

    @ApiModelProperty(value = "是否已结束")
    @TableField("is_finished")
    private Boolean isFinished;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("current_step")
    private String currentStep;

    @TableField("total_step")
    private String totalStep;

    public static final String ID = "id";

    public static final String LET_CLUE_ID = "let_clue_id";

    public static final String TITLE = "title";

    public static final String BWLD_CONTENT = "bwld_content";

    public static final String BWLD_ID = "bwld_id";

    public static final String BWLD_NAME = "bwld_name";

    public static final String BWLD_SIGN = "bwld_sign";

    public static final String BWLD_DATE = "bwld_date";

    public static final String FGFSJ_CONTENT = "fgfsj_content";

    public static final String FGFSJ_ID = "fgfsj_id";

    public static final String FGFSJ_NAME = "fgfsj_name";

    public static final String FGFSJ_SIGN = "fgfsj_sign";

    public static final String FGFSJ_DATE = "fgfsj_date";

    public static final String ZGCW_CONTENT = "zgcw_content";

    public static final String ZGCW_ID = "zgcw_id";

    public static final String ZGCW_NAME = "zgcw_name";

    public static final String ZGCW_SIGN = "zgcw_sign";

    public static final String ZGCW_DATE = "zgcw_date";

    public static final String SCS_CONTENT = "scs_content";

    public static final String SCS_ID = "scs_id";

    public static final String SCS_NAME = "scs_name";

    public static final String SCS_SIGN = "scs_sign";

    public static final String SCS_DATE = "scs_date";

    public static final String OPERATOR = "operator";

    public static final String OPERATOR_PHONE = "operator_phone";

    public static final String IS_FINISHED = "is_finished";

    public static final String CREATE_DATE = "create_date";

    public static final String CURRENT_STEP = "current_step";

    public static final String TOTAL_STEP = "total_step";

}
