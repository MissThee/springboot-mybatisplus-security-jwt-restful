package com.github.missthee.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.missthee.db.dto.manage.permission.PermissionInsertOneDTO;
import com.github.missthee.db.dto.manage.permission.PermissionUpdateOneDTO;
import com.github.missthee.db.entity.primary.manage.Permission;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;

public interface PermissionService extends IService<Permission> {
    Long insertOne(PermissionInsertOneDTO insertOneReq);

    Boolean deleteOne(Long id);

    Boolean updateOne(PermissionUpdateOneDTO updateOneReq);

    Permission selectOne(Long id);

    List<Permission> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean isDuplicate(String permission);

    Boolean isDuplicateExceptSelf(String permission, Long id);
}
