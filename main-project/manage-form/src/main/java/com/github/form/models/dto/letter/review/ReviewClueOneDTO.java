package com.github.form.models.dto.letter.review;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ReviewClueOneDTO {

    @ApiModelProperty(value = "线索id")
    private String letClueId;

    @ApiModelProperty(value = "文件标题")
    private String title;

    @ApiModelProperty(value = "本委领导批示")
    private String bwldContent;

    @ApiModelProperty(value = "本委领导id")
    private Integer bwldId;

    @ApiModelProperty(value = "本委领导姓名")
    private String bwldName;

    @ApiModelProperty(value = "本委领导签字")
    @TableField("bwld_sign")
    private String bwldSign;

    @ApiModelProperty(value = "本委领导批示时间")
    private LocalDateTime bwldDate;

    @ApiModelProperty(value = "分管副书记意见")
    private String fgfsjContent;

    @ApiModelProperty(value = "分管副书记d")
    private Integer fgfsjId;

    @ApiModelProperty(value = "分管副书记名字")
    private String fgfsjName;

    @ApiModelProperty(value = "分管副书记签字")
    private String fgfsjSign;

    @ApiModelProperty(value = "分管副书记意见时间")
    private LocalDateTime fgfsjDate;

    @ApiModelProperty(value = "主管常委意见")
    private String zgcwContent;

    @ApiModelProperty(value = "主管常委id")
    private Integer zgcwId;

    @ApiModelProperty(value = "主管常委名字")
    private String zgcwName;

    @ApiModelProperty(value = "主管常委签字")
    private String zgcwSign;

    @ApiModelProperty(value = "主管常委意见时间")
    private LocalDateTime zgcwDate;

    @ApiModelProperty(value = "审查室意见")
    private String scsContent;

    @ApiModelProperty(value = "审查室id")
    private Integer scsId;

    @ApiModelProperty(value = "审查室名字")
    private String scsName;

    @ApiModelProperty(value = "审查室签字")
    private String scsSign;

    @ApiModelProperty(value = "审查室意见时间")
    private LocalDateTime scsDate;

    @ApiModelProperty(value = "经办人")
    private String operator;

    @ApiModelProperty(value = "联系电话")
    private String operatorPhone;

    @ApiModelProperty(value = "是否已结束")
    private Boolean isFinished;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;
}