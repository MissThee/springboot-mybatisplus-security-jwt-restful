package com.github.common.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.common.dto.manage.user.UserInTableDTO;
import com.github.common.dto.manage.user.UserInTableDetailDTO;
import com.github.common.dto.manage.user.UserInsertOneDTO;
import com.github.common.dto.manage.user.UserUpdateOneDTO;
import com.github.common.db.entity.primary.manage.User;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;

public interface UserService extends IService<User> {
    Long insertOne(UserInsertOneDTO insertOneReq);

    Boolean deleteOne(Long id);

    Boolean updateOne(UserUpdateOneDTO updateOneReq);

    UserInTableDetailDTO selectOne(Long id);

    List<UserInTableDTO> selectList(Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean isExist(String username);
}
