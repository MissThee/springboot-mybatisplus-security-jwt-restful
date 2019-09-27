package com.github.form.service.interf.letter.dictionary;

import com.github.common.db.entity.primary.DicJobType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;

import java.util.List;

/**
 * <p>
 * 干部类型表（是否干部） 服务类
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-03
 */
public interface DicJobTypeService extends IService<DicJobType> {
    Integer insertOne(DicCommonInsertDTO dicCommonInsertDTO);

    Boolean deleteOne(Integer id);

    Boolean updateOne(DicCommonUpdateDTO dicCommonUpdateDTO);

    List<DicJobType> selectList(Boolean isDelete);
}
