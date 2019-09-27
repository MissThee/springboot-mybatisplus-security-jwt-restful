package com.github.form.db.mapper.primary.letter.dictionary;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.common.db.entity.primary.DicResultType;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 信访信访室处理结果 Mapper 接口
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-03
 */
@Component
public interface DicResultTypeMapper extends BaseMapper<DicResultType> {
    DicResultType selectByResultTypeId(Integer id);
}
