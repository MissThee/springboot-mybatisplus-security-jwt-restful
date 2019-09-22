package com.github.missthee.service.interf.manage;

import com.github.missthee.vo.manage.UserControllerVO;
import com.github.missthee.db.entity.primary.manage.User;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserService {
    Long insertOne(UserControllerVO.InsertOneReq insertOneReq);

    Boolean deleteOne(Long id);

    Boolean updateOne(UserControllerVO.UpdateOneReq updateOneReq);

    User selectOne(Long id);

    List<User> selectList(UserControllerVO.SelectListReq findAllReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;


    Boolean deleteOnePhysical(Long id);
}
