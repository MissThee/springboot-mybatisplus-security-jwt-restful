package com.github.form.service.interf.letter.dictionary;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.common.db.entity.primary.DicSource;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;

import java.util.List;

/**
 * <p>
 * 线索来源 服务类
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-03
 */
public interface DicSourceService extends IService<DicSource> {
    Integer insertOne(DicCommonInsertDTO dicCommonInsertDTO);

    Boolean deleteOne(Integer id);

    Boolean updateOne(DicCommonUpdateDTO dicCommonUpdateDTO);

    List<DicSource> selectList(Boolean isDelete);
}
