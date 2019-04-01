package com.github.missthee.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.missthee.db.dto.manage.rolecontroller.InsertOneReq;
import com.github.missthee.db.dto.manage.rolecontroller.SelectListReq;
import com.github.missthee.db.dto.manage.rolecontroller.UpdateOneReq;
import com.github.missthee.db.po.primary.manage.Role;
import com.github.missthee.db.po.primary.manage.Role;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface RoleService extends IService<Role> {
    Long insertOne(InsertOneReq insertOneReq);

    Boolean deleteOne(Long id);

    Boolean updateOne(UpdateOneReq updateOneReq);

    Role selectOne(Long id);

    List<Role> selectList(SelectListReq findAllReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean isDuplicate(String role);

    Boolean isDuplicateExceptSelf(String role, Long id);
}
