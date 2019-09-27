package com.github.form.db.mapper.primary.letter.dictionary;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.common.db.entity.primary.DicAreaInvolved;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 涉及领域 Mapper 接口
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-04
 */
@Component
public interface DicAreaInvolvedMapper extends BaseMapper<DicAreaInvolved> {
    List<DicAreaInvolved> selectByIllegalClueAreaInvolvedIdS(@Param("illegalClueAreaInvolvedIdS") List<Integer> illegalClueAreaInvolvedIdS);
}
