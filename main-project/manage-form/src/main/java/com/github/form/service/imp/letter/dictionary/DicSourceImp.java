package com.github.form.service.imp.letter.dictionary;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.common.db.entity.primary.DicSource;
import com.github.form.db.mapper.primary.letter.dictionary.DicSourceMapper;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;
import com.github.form.service.interf.letter.dictionary.DicSourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 线索来源 服务实现类
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-03
 */
@Service
public class DicSourceImp extends ServiceImpl<DicSourceMapper, DicSource> implements DicSourceService {
    private final DicSourceMapper dicSourceMapper;
    private final MapperFacade mapperFacade;

    @Autowired
    public DicSourceImp(MapperFacade mapperFacade, DicSourceMapper dicSourceMapper) {
        this.mapperFacade = mapperFacade;
        this.dicSourceMapper = dicSourceMapper;
    }

    @Override
    public Integer insertOne(DicCommonInsertDTO dicCommonInsertDTO) {
        DicSource dicSource = mapperFacade.map(dicCommonInsertDTO, DicSource.class);
        dicSourceMapper.insert(dicSource);
        return dicSource.getId();
    }

    @Override
    public Boolean deleteOne(Integer id) {
        if (id == null) {
            return false;
        }
        return dicSourceMapper.updateById(new DicSource().setId(id).setIsDelete(true)) > 0;

    }

    @Override
    public Boolean updateOne(DicCommonUpdateDTO dicCommonUpdateDTO) {
        DicSource dicSource = mapperFacade.map(dicCommonUpdateDTO, DicSource.class);
        return (dicSourceMapper.updateById(dicSource) > 0);
    }

    @Override
    public List<DicSource> selectList(Boolean isDelete) {
        QueryWrapper<DicSource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DicSource.IS_DELETE, isDelete)
                .orderBy(true, true, DicSource.INDEX_NUMBER);
        return dicSourceMapper.selectList(queryWrapper);
    }
}
