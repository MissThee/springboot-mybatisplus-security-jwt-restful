package com.github.base.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.base.dto.manage.permission.SysPermissionInsertOneDTO;
import com.github.base.dto.manage.permission.SysPermissionUpdateOneDTO;
import com.github.common.db.entity.primary.SysPermission;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;

public interface PermissionService extends IService<SysPermission> {
    Long insertOne(SysPermissionInsertOneDTO permissionInsertOneDTO);

    Boolean deleteOne(Long id);

    Boolean updateOne(SysPermissionUpdateOneDTO permissionUpdateOneDTO);

    SysPermission selectOne(Long id);

    List<SysPermission> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean isExist(String permission);

    Boolean isExistExceptSelf(String permission, Long id);
}
