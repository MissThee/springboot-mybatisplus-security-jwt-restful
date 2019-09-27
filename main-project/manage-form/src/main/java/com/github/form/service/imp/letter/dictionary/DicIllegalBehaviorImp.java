package com.github.form.service.imp.letter.dictionary;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.common.db.entity.primary.DicIllegalBehavior;
import com.github.form.db.mapper.primary.letter.dictionary.DicIllegalBehaviorMapper;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;
import com.github.form.service.interf.letter.dictionary.DicIllegalBehaviorService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 违法行为 服务实现类
 * </p>
 *
 * @author WORK, MT
 * @since 2019-06-03
 */
@Service
public class DicIllegalBehaviorImp extends ServiceImpl<DicIllegalBehaviorMapper, DicIllegalBehavior> implements DicIllegalBehaviorService {
    private final DicIllegalBehaviorMapper dicIllegalBehaviorMapper;
    private final MapperFacade mapperFacade;

    @Autowired
    public DicIllegalBehaviorImp(MapperFacade mapperFacade, DicIllegalBehaviorMapper dicIllegalBehaviorMapper) {
        this.mapperFacade = mapperFacade;
        this.dicIllegalBehaviorMapper = dicIllegalBehaviorMapper;
    }

    @Override
    public Integer insertOne(DicCommonInsertDTO dicCommonInsertDTO) {
        DicIllegalBehavior dicIllegalBehavior = mapperFacade.map(dicCommonInsertDTO, DicIllegalBehavior.class);
        dicIllegalBehaviorMapper.insert(dicIllegalBehavior);
        return dicIllegalBehavior.getId();
    }

    @Override
    public Boolean deleteOne(Integer id) {
        if (id == null) {
            return false;
        }
        return   dicIllegalBehaviorMapper.updateById(new DicIllegalBehavior().setId(id).setIsDelete(true)) > 0;
    }

    @Override
    public Boolean updateOne(DicCommonUpdateDTO dicCommonUpdateDTO) {
        DicIllegalBehavior dicIllegalBehavior = mapperFacade.map(dicCommonUpdateDTO, DicIllegalBehavior.class);
        return (dicIllegalBehaviorMapper.updateById(dicIllegalBehavior) > 0);
    }

    @Override
    public List<DicIllegalBehavior> selectList(Boolean isDelete) {
        QueryWrapper<DicIllegalBehavior> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DicIllegalBehavior.IS_DELETE, isDelete)
                .orderBy(true, true, DicIllegalBehavior.INDEX_NUMBER);
        return dicIllegalBehaviorMapper.selectList(queryWrapper);
    }
}
