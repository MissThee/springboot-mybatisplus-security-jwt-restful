package com.github.base.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.base.dto.manage.user.SysUserInTableDTO;
import com.github.base.dto.manage.user.SysUserInTableDetailDTO;
import com.github.base.dto.manage.user.SysUserInsertOneDTO;
import com.github.base.dto.manage.user.SysUserUpdateOneDTO;
import com.github.common.db.entity.primary.SysUser;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;

public interface UserService extends IService<SysUser> {
    Long insertOne(SysUserInsertOneDTO userInsertOneDTO);

    Boolean deleteOne(Long id);

    Boolean updateOne(SysUserUpdateOneDTO userUpdateOneDTO);

    SysUserInTableDetailDTO selectOne(Long id);

    List<SysUserInTableDTO> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean isExist(String username);

    Boolean resetDefaultPassword(Long id);
}
