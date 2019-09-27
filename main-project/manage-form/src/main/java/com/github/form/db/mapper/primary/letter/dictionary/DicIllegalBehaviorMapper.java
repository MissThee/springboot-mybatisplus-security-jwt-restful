package com.github.form.db.mapper.primary.letter.dictionary;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.common.db.entity.primary.DicIllegalBehavior;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 违法行为 Mapper 接口
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-03
 */
@Component
public interface DicIllegalBehaviorMapper extends BaseMapper<DicIllegalBehavior> {
    List<DicIllegalBehavior> selectByIllegalBehaviorId(@Param("illegalBehaviorIdS") List<Integer> illegalBehaviorIdS);
}
