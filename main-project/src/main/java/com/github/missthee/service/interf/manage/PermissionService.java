package com.github.missthee.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.missthee.db.dto.manage.permissioncontroller.InsertOneReq;
import com.github.missthee.db.dto.manage.permissioncontroller.SelectTreeReq;
import com.github.missthee.db.dto.manage.permissioncontroller.UpdateOneReq;
import com.github.missthee.db.po.primary.manage.Permission;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface PermissionService extends IService<Permission> {
    Long insertOne(InsertOneReq insertOneReq);

    Boolean deleteOne(Long id);

    Boolean updateOne(UpdateOneReq updateOneReq);

    Permission selectOne(Long id);

    List<Permission> selectList(SelectTreeReq findAllReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean isDuplicate(String permission);

    Boolean isDuplicateExceptSelf(String permission, Long id);
}
