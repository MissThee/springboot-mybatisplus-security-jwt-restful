package com.github.base.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.base.dto.manage.role.RoleInsertOneDTO;
import com.github.base.dto.manage.role.RoleUpdateOneDTO;
import com.github.common.db.entity.primary.manage.Role;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;

public interface RoleService extends IService<Role> {
    Long insertOne(RoleInsertOneDTO insertOneReq);

    Boolean deleteOne(Long id);

    Boolean updateOne(RoleUpdateOneDTO roleUpdateOneDTO);

    Role selectOne(Long id);

    List<Role> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean isExist(String role);

    Boolean isExistExceptSelf(String role, Long id);
}
