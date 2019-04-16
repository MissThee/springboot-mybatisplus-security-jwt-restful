package com.github.missthee.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.missthee.db.dto.manage.user.UserInTableDTO;
import com.github.missthee.db.dto.manage.user.UserInTableDetailDTO;
import com.github.missthee.db.dto.manage.user.UserInsertOneDTO;
import com.github.missthee.db.dto.manage.user.UserUpdateOneDTO;
import com.github.missthee.db.vo.manage.UserVO;
import com.github.missthee.db.entity.primary.manage.User;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserService extends IService<User> {
    Long insertOne(UserInsertOneDTO insertOneReq);

    Boolean deleteOne(Long id);

    Boolean updateOne(UserUpdateOneDTO updateOneReq);

    UserInTableDetailDTO selectOne(Long id);

    List<UserInTableDTO> selectList(UserVO.SelectListReq findAllReq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean isDuplicate(String username);
}
