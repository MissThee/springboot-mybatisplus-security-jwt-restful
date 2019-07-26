package com.github.base.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.base.dto.manage.permission.PermissionInsertOneDTO;
import com.github.base.dto.manage.permission.PermissionUpdateOneDTO;
import com.github.common.db.entity.primary.Permission;
import org.springframework.stereotype.Service;

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

    Boolean isExist(String permission);

    Boolean isExistExceptSelf(String permission, Long id);
}
