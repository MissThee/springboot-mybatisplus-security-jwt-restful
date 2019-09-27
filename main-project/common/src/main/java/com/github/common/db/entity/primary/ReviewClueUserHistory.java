package com.github.common.db.entity.primary;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@ApiModel(value="ReviewClueUserHistory对象", description="")
public class ReviewClueUserHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="id", type = IdType.AUTO)
    private Integer id;

    @TableField("review_id")
    private Long reviewId;

    @TableField("user_id")
    private Long userId;


    public static final String ID = "id";

    public static final String REVIEW_ID = "review_id";

    public static final String USER_ID = "user_id";

}
