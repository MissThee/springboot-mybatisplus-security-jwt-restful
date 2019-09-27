package com.github.form.service.imp.letter.dictionary;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.common.db.entity.primary.DicJobType;
import com.github.form.db.mapper.primary.letter.dictionary.DicJobTypeMapper;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;
import com.github.form.service.interf.letter.dictionary.DicJobTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 干部类型表（是否干部） 服务实现类
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-03
 */
@Service
public class DicJobTypeImp extends ServiceImpl<DicJobTypeMapper, DicJobType> implements DicJobTypeService {
    private final DicJobTypeMapper dicJobTypeMapper;
    private final MapperFacade mapperFacade;

    @Autowired
    public DicJobTypeImp(MapperFacade mapperFacade, DicJobTypeMapper dicJobTypeMapper) {
        this.mapperFacade = mapperFacade;
        this.dicJobTypeMapper = dicJobTypeMapper;
    }

    @Override
    public Integer insertOne(DicCommonInsertDTO dicCommonInsertDTO) {
        DicJobType dicJobType = mapperFacade.map(dicCommonInsertDTO, DicJobType.class);
        dicJobTypeMapper.insert(dicJobType);
        return dicJobType.getId();
    }

    @Override
    public Boolean deleteOne(Integer id) {
        if (id == null) {
            return false;
        }
        return dicJobTypeMapper.updateById(new DicJobType().setId(id).setIsDelete(true)) > 0;

    }

    @Override
    public Boolean updateOne(DicCommonUpdateDTO dicCommonUpdateDTO) {
        DicJobType dicJobType = mapperFacade.map(dicCommonUpdateDTO, DicJobType.class);
        return (dicJobTypeMapper.updateById(dicJobType) > 0);
    }

    @Override
    public List<DicJobType> selectList(Boolean isDelete) {
        QueryWrapper<DicJobType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DicJobType.IS_DELETE, isDelete)
                .orderBy(true, true, DicJobType.INDEX_NUMBER);
        return dicJobTypeMapper.selectList(queryWrapper);
    }
}
