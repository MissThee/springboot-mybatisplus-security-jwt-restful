package com.github.form.service.interf.letter.dictionary;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.common.db.entity.primary.DicResultType;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;

import java.util.List;

/**
 * <p>
 * 信访信访室处理结果 服务类
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-03
 */
public interface DicResultTypeService extends IService<DicResultType> {
    Integer insertOne(DicCommonInsertDTO dicCommonInsertDTO);

    Boolean deleteOne(Integer id);

    Boolean updateOne(DicCommonUpdateDTO dicCommonUpdateDTO);

    List<DicResultType> selectList(Boolean isDelete);
}
