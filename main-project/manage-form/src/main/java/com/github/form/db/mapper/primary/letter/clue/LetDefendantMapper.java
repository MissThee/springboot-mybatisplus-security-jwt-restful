package com.github.form.db.mapper.primary.letter.clue;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.common.db.entity.primary.LetDefendant;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * <p>
 * 被反映人 Mapper 接口
 * </p>
 *
 * @author DESKTOP-3Q631SR,WLW
 * @since 2019-06-05
 */
@Component
public interface LetDefendantMapper extends BaseMapper<LetDefendant>  {
    //注入的自定义mysql批量插入方法，可能会检测无mapper映射，可正常运行
    int insertBatch(@Param(value = "items") Collection idList);
}
