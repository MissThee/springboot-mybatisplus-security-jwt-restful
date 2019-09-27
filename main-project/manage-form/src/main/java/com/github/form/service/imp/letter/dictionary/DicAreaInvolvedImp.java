package com.github.form.service.imp.letter.dictionary;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.common.db.entity.primary.DicAreaInvolved;
import com.github.form.db.mapper.primary.letter.dictionary.DicAreaInvolvedMapper;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;
import com.github.form.service.interf.letter.dictionary.DicAreaInvolvedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 涉及领域 服务实现类
 * </p>
 *
 * @author WORK,MT
 * @since 2019-06-04
 */
@Service
public class DicAreaInvolvedImp extends ServiceImpl<DicAreaInvolvedMapper, DicAreaInvolved> implements DicAreaInvolvedService {
    private final DicAreaInvolvedMapper dicAreaInvolvedMapper;
    private final MapperFacade mapperFacade;

    @Autowired
    public DicAreaInvolvedImp(MapperFacade mapperFacade, DicAreaInvolvedMapper dicAreaInvolvedMapper) {
        this.mapperFacade = mapperFacade;
        this.dicAreaInvolvedMapper = dicAreaInvolvedMapper;
    }

    @Override
    public Integer insertOne(DicCommonInsertDTO dicCommonInsertDTO) {
        DicAreaInvolved dicAreaInvolved = mapperFacade.map(dicCommonInsertDTO, DicAreaInvolved.class);
        dicAreaInvolvedMapper.insert(dicAreaInvolved);
        return dicAreaInvolved.getId();
    }

    @Override
    public Boolean deleteOne(Integer id) {
        if (id == null) {
            return false;
        }
        return dicAreaInvolvedMapper.updateById(new DicAreaInvolved().setId(id).setIsDelete(true)) > 0;

    }

    @Override
    public Boolean updateOne(DicCommonUpdateDTO dicCommonUpdateDTO) {
        DicAreaInvolved dicAreaInvolved = mapperFacade.map(dicCommonUpdateDTO, DicAreaInvolved.class);
        return (dicAreaInvolvedMapper.updateById(dicAreaInvolved) > 0);
    }

    @Override
    public List<DicAreaInvolved> selectList(Boolean isDelete) {
        QueryWrapper<DicAreaInvolved> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DicAreaInvolved.IS_DELETE, isDelete)
                .orderBy(true, true, DicAreaInvolved.INDEX_NUMBER);
        return dicAreaInvolvedMapper.selectList(queryWrapper);
    }
}
