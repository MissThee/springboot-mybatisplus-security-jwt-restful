package com.github.form.db.mapper.primary.letter.dictionary;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.common.db.entity.primary.DicSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 线索来源 Mapper 接口
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-03
 */
@Component
public interface DicSourceMapper extends BaseMapper<DicSource> {
    List<DicSource> selectBySourceIds(@Param("sourceIds") List<Integer> sourceIds);
}
