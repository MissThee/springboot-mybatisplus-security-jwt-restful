package com.github.form.service.imp.letter.dictionary;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.common.db.entity.primary.DicJobRank;
import com.github.form.db.mapper.primary.letter.dictionary.DicJobRankMapper;
import com.github.form.models.dto.letter.dictionary.DicCommonInsertDTO;
import com.github.form.models.dto.letter.dictionary.DicCommonUpdateDTO;
import com.github.form.service.interf.letter.dictionary.DicJobRankService;
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
 * @author WORK, MT
 * @since 2019-06-04
 */
@Service
public class DicJobRankImp extends ServiceImpl<DicJobRankMapper, DicJobRank> implements DicJobRankService {
    private final DicJobRankMapper dicJobRankMapper;
    private final MapperFacade mapperFacade;

    @Autowired
    public DicJobRankImp(MapperFacade mapperFacade, DicJobRankMapper dicJobRankMapper) {
        this.mapperFacade = mapperFacade;
        this.dicJobRankMapper = dicJobRankMapper;
    }

    @Override
    public Integer insertOne(DicCommonInsertDTO dicCommonInsertDTO) {
        DicJobRank dicJobRank = mapperFacade.map(dicCommonInsertDTO, DicJobRank.class);
        dicJobRankMapper.insert(dicJobRank);
        return dicJobRank.getId();
    }

    @Override
    public Boolean deleteOne(Integer id) {
        if (id == null) {
            return false;
        }
        return dicJobRankMapper.updateById(new DicJobRank().setId(id).setIsDelete(true)) > 0;
    }

    @Override
    public Boolean updateOne(DicCommonUpdateDTO dicCommonUpdateDTO) {
        DicJobRank dicJobRank = mapperFacade.map(dicCommonUpdateDTO, DicJobRank.class);
        return (dicJobRankMapper.updateById(dicJobRank) > 0);
    }

    @Override
    public List<DicJobRank> selectList(Boolean isDelete) {
        QueryWrapper<DicJobRank> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DicJobRank.IS_DELETE, isDelete)
                .orderBy(true, true, DicJobRank.INDEX_NUMBER);
        return dicJobRankMapper.selectList(queryWrapper);
    }
}
