package com.github.form.db.mapper.primary.letter.clue;

import com.github.common.db.entity.primary.LetClueAreaInvolved;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * <p>
 * 信访表单和设计领域关联表 Mapper 接口
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
@Component
public interface LetClueAreaInvolvedMapper extends BaseMapper<LetClueAreaInvolved>{
    //注入的自定义mysql批量插入方法，可能会检测无mapper映射，可正常运行
    int insertBatch(@Param(value = "items") Collection idList);
}
