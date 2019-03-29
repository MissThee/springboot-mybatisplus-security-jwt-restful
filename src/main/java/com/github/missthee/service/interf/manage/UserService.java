package com.github.missthee.service.interf.manage;

import com.github.missthee.db.dto.manage.usercontroller.InsertOneReq;
import com.github.missthee.db.dto.manage.usercontroller.SelectListReq;
import com.github.missthee.db.dto.manage.usercontroller.UpdateOneReq;
import com.github.missthee.db.po.primary.manage.User;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserService {
    Long insertOne(InsertOneReq insertOneReq);

    Boolean deleteOne(Long id);

    Boolean updateOne(UpdateOneReq updateOneReq);

    User selectOne(Long id);

    List<User> selectList(SelectListReq findAllReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;


    Boolean deleteOnePhysical(Long id);
}
