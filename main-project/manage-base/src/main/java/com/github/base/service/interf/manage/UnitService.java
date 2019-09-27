package com.github.base.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.base.dto.manage.unit.SysUnitInsertOneDTO;
import com.github.base.dto.manage.unit.SysUnitUpdateOneDTO;
import com.github.common.db.entity.primary.SysUnit;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author WORK, MT
 * @since 2019-04-15
 */
public interface UnitService extends IService<SysUnit> {

    Long insertOne(SysUnitInsertOneDTO unitInsertOneDTO);

    Boolean deleteOne(Long id);

    Boolean deleteOnePhysical(Long id);

    Boolean updateOne(SysUnitUpdateOneDTO unitUpdateOneDTO);

    SysUnit selectOne(Long id);

    List<SysUnit> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy);

    List<SysUnit> getListByType(String type);
}
