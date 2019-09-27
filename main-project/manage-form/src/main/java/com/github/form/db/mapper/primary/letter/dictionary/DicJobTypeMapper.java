package com.github.form.db.mapper.primary.letter.dictionary;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.common.db.entity.primary.DicJobType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 干部类型表（是否干部） Mapper 接口
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-03
 */
@Component
public interface DicJobTypeMapper extends BaseMapper<DicJobType> {
    List<DicJobType> selectByJobTypeIdS(@Param("jobTypeIdS") List<Integer> jobTypeIdS);
}
