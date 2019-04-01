package com.github.missthee.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.missthee.db.dto.manage.usercontroller.InsertOneReq;
import com.github.missthee.db.dto.manage.usercontroller.SelectListReq;
import com.github.missthee.db.dto.manage.usercontroller.UpdateOneReq;
import com.github.missthee.db.po.primary.manage.User;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserService  extends IService<User> {
    Long insertOne(InsertOneReq insertOneReq);

    Boolean deleteOne(Long id);

    Boolean updateOne(UpdateOneReq updateOneReq);

    User selectOne(Long id);

    List<User> selectList(SelectListReq findAllReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean isDuplicate(String username);
}
