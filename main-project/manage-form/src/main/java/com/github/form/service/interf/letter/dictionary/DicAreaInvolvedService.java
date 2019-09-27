package com.github.form.service.interf.letter.dictionary;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.common.db.entity.primary.DicAreaInvolved;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;

import java.util.List;

/**
 * <p>
 * 涉及领域 服务类
 * </p>
 *
 * @author WORK, MT
 * @since 2019-06-04
 */
public interface DicAreaInvolvedService extends IService<DicAreaInvolved> {
    Integer insertOne(DicCommonInsertDTO dicCommonInsertDTO);

    Boolean deleteOne(Integer id);

    Boolean updateOne(DicCommonUpdateDTO dicCommonUpdateDTO);

    List<DicAreaInvolved> selectList(Boolean isDelete);
}
