package com.github.base.service.imp.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.base.dto.manage.unit.UnitInsertOneDTO;
import com.github.base.dto.manage.unit.UnitUpdateOneDTO;
import com.github.common.db.entity.primary.manage.Unit;
import com.github.common.db.mapper.primary.manage.UnitMapper;
import com.github.base.service.interf.manage.UnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author WORK, MT
 * @since 2019-04-15
 */
@Service
public class UnitImp extends ServiceImpl<UnitMapper, Unit> implements UnitService {
    private final MapperFacade mapperFacade;
    private final UnitMapper unitMapper;

    @Autowired
    public UnitImp(UnitMapper unitMapper, MapperFacade mapperFacade) {
        this.unitMapper = unitMapper;
        this.mapperFacade = mapperFacade;
    }

    @Override
    public Long insertOne(UnitInsertOneDTO unitInsertOneDTO) {
        Unit unit = mapperFacade.map(unitInsertOneDTO, Unit.class);
        unitMapper.insert(unit);
        Long unitId = unit.getId();
        return unitId;
    }

    @Override
    public Boolean deleteOne(Long id) {
        if (id == null) {
            return false;
        }
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Unit.ID, id)
                .eq(Unit.IS_DELETE, id);
        Boolean result = unitMapper.delete(queryWrapper) > 0;
        return result;
    }

    @Override
    public Boolean updateOne(UnitUpdateOneDTO unitUpdateOneDTO) {
        //拷贝用户信息，生成Unit对象
        Unit unit = mapperFacade.map(unitUpdateOneDTO, Unit.class);
        //更新信息
        Boolean result = unitMapper.updateById(unit) > 0;
        return result;
    }

    @Override
    public Unit selectOne(Long id) {
        return unitMapper.selectById(id);
    }

    @Override
    public List<Unit> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        if (!isDelete) {//相较于user和role，unit不能仅显示已删除节点，因为仅已删除的节点不能构建完整的树结构
            queryWrapper.eq(Unit.IS_DELETE, isDelete);
        }
        if (orderBy != null) {
            for (Map.Entry<String, Boolean> entry : orderBy.entrySet()) {
                queryWrapper.orderBy(true, entry.getValue(), entry.getKey());
            }
        }
        return unitMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean deleteOnePhysical(Long id) {
        return unitMapper.deleteById(id) > 0;
    }
}
