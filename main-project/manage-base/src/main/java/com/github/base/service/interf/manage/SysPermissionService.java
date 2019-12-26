package com.github.base.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.base.vo.manage.SysPermissionVO;
import com.github.common.db.entity.primary.SysPermission;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {
    Long insertOne(SysPermissionVO.InsertOneReq insertOneReq);

    Boolean deleteOne(Long id);

    Boolean updateOne(SysPermissionVO.UpdateOneReq  updateOneReq);

    SysPermission selectOne(Long id);

    List<SysPermission> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean isExist(String permission);

    Boolean isExistExceptSelf(String permission, Long id);
}
