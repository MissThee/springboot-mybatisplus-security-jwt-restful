package com.github.base.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.base.dto.manage.myaccount.MyAccountComparePasswordDTO;
import com.github.base.dto.manage.myaccount.MyAccountUpdatePasswordDTO;
import com.github.base.dto.manage.user.UserInTableDTO;
import com.github.base.dto.manage.user.UserInTableDetailDTO;
import com.github.base.dto.manage.user.UserInsertOneDTO;
import com.github.base.dto.manage.user.UserUpdateOneDTO;
import com.github.base.vo.manage.MyAccountVO;
import com.github.common.db.entity.primary.manage.User;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;

public interface MyAccountService extends IService<User> {
    Boolean comparePassword(MyAccountComparePasswordDTO myAccountComparePasswordDTO);

    Boolean updatePassword(MyAccountUpdatePasswordDTO myAccountUpdatePasswordDTO);

}
