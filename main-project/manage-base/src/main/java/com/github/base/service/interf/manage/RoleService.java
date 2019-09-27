package com.github.base.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.base.dto.manage.role.SysRoleInTableDetailDTO;
import com.github.base.dto.manage.role.SysRoleInsertOneDTO;
import com.github.base.dto.manage.role.SysRoleUpdateOneDTO;
import com.github.common.db.entity.primary.SysRole;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;

public interface RoleService extends IService<SysRole> {
    Long insertOne(SysRoleInsertOneDTO roleInsertOneDTO);

    Boolean deleteOne(Long id);

    Boolean updateOne(SysRoleUpdateOneDTO roleUpdateOneDTO);

    SysRoleInTableDetailDTO selectOne(Long id);

    List<SysRole> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean isExist(String role);

    Boolean isExistExceptSelf(String role, Long id);
}
