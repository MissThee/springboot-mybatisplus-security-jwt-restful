package com.github.base.service.imp.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.base.db.mapper.primary.manage.SysUnitMapper;
import com.github.base.dto.manage.unit.SysUnitInsertOneDTO;
import com.github.base.dto.manage.unit.SysUnitUpdateOneDTO;
import com.github.base.service.interf.manage.SysUnitService;
import com.github.common.db.entity.primary.SysUnit;
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
public class SysUnitImp extends ServiceImpl<SysUnitMapper, SysUnit> implements SysUnitService {
    private final MapperFacade mapperFacade;
    private final SysUnitMapper unitMapper;

    @Autowired
    public SysUnitImp(SysUnitMapper unitMapper, MapperFacade mapperFacade) {
        this.unitMapper = unitMapper;
        this.mapperFacade = mapperFacade;
    }

    @Override
    public Long insertOne(SysUnitInsertOneDTO unitInsertOneDTO) {
        SysUnit unit = mapperFacade.map(unitInsertOneDTO, SysUnit.class);
        unitMapper.insert(unit);
        Long unitId = unit.getId();
        return unitId;
    }

    @Override
    public Boolean deleteOne(Long id) {
        if (id == null) {
            return false;
        }
        Boolean result = unitMapper.updateById(new SysUnit().setId(id).setIsDelete(true)) > 0;
        return result;
    }

    @Override
    public Boolean updateOne(SysUnitUpdateOneDTO unitUpdateOneDTO) {
        //拷贝用户信息，生成Unit对象
        SysUnit unit = mapperFacade.map(unitUpdateOneDTO, SysUnit.class);
        //更新信息
        Boolean result = unitMapper.updateById(unit) > 0;
        return result;
    }

    @Override
    public SysUnit selectOne(Long id) {
        return unitMapper.selectById(id);
    }

    @Override
    public List<SysUnit> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) {
        QueryWrapper<SysUnit> queryWrapper = new QueryWrapper<>();
        if (!isDelete) {//相较于user和role，unit不能仅显示已删除节点，因为仅已删除的节点不能构建完整的树结构
            queryWrapper.eq(SysUnit.IS_DELETE, isDelete);
        }
        queryWrapper.orderBy(true, true, SysUnit.INDEX_NUM);
        if (orderBy != null) {
            for (Map.Entry<String, Boolean> entry : orderBy.entrySet()) {
                queryWrapper.orderBy(true, entry.getValue(), entry.getKey());
            }
        }
        return unitMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysUnit> getListByType(String type) {
        QueryWrapper<SysUnit> qw = new QueryWrapper<>();
        qw.eq(SysUnit.TYPE, type);
        return unitMapper.selectList(qw);
    }

    @Override
    public Boolean deleteOnePhysical(Long id) {
        return unitMapper.deleteById(id) > 0;
    }
}
