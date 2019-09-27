package com.github.common.db.entity.primary;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 信访表单
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "LetClue对象", description = "信访表单")
public class LetClue implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "线索编码")
    @TableId("id")
    private String id;

    @ApiModelProperty(value = "受理时间")
    @TableField("reception_time")
    private LocalDate receptionTime;

    @ApiModelProperty(value = "反映的主要问题（文本输入）")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_date")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "已处理")
    @TableField("is_processed")
    private Boolean isProcessed;

    @ApiModelProperty(value = "分类结果")
    @TableField("result_type_id")
    private Integer resultTypeId;

    @ApiModelProperty(value = "信访整理流程进度id")
    @TableField("state_id")
    private Integer stateId;

    @ApiModelProperty(value = "已删状态")
    @TableField("is_delete")
    private Boolean isDelete;

    @ApiModelProperty(value = "是否事件")
    @TableField("is_event")
    private Boolean isEvent;

    public static final String ID = "id";

    public static final String RECEPTION_TIME = "reception_time";

    public static final String CONTENT = "content";

    public static final String CREATE_DATE = "create_date";

    public static final String IS_PROCESSED = "is_processed";

    public static final String RESULT_TYPE_ID = "result_type_id";

    public static final String STATE_ID = "state_id";

    public static final String IS_DELETE = "is_delete";

    public static final String IS_event = "is_delete";

}
