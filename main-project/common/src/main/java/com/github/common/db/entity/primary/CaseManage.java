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
 *
 * </p>
 *
 * @author WORK-PC,MT
 * @since 2019-09-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CaseManage对象", description = "")
public class CaseManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "线索id")
    @TableField("let_clue_id")
    private String letClueId;

    @ApiModelProperty(value = "意见")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "是否要结果件")
    @TableField("is_need_result_form")
    private Boolean isNeedResultForm;

    @ApiModelProperty(value = "要结果件时，限制的时间")
    @TableField("result_form_limit_date")
    private LocalDateTime resultFormLimitDate;

    @ApiModelProperty(value = "分派给案管室的1-15编号")
    @TableField("send_to_sub_dept_id")
    private Integer sendToSubDeptId;

    @ApiModelProperty(value = "办理状态：0-默认未办理，1-已办理，2-已驳回")
    @TableField("state_id")
    private Integer stateId;

    @ApiModelProperty(value = "最后处理时间（手动记录）")
    @TableField("operation_date")
    private LocalDateTime operationDate;

    @ApiModelProperty(value = "创建时间（自动记录）")
    @TableField("create_date")
    private LocalDateTime createDate;

    public static final String ID = "id";

    public static final String LET_CLUE_ID = "let_clue_id";

    public static final String CONTENT = "content";

    public static final String IS_NEED_RESULT_FORM = "is_need_result_form";

    public static final String RESULT_FORM_LIMIT_DATE = "result_form_limit_date";

    public static final String SEND_TO_SUB_DEPT_ID = "send_to_sub_dept_id";

    public static final String STATE_ID = "state_id";

    public static final String OPERATION_DATE = "operation_date";

    public static final String CREATE_DATE = "create_date";

}
