package com.github.base.service.imp.manage;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.base.service.interf.manage.MyAccountService;
import com.github.common.db.entity.primary.User;
import com.github.base.db.mapper.primary.manage.*;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(rollbackFor = {Exception.class})
public class MyAccountImp extends ServiceImpl<UserMapper, User> implements MyAccountService {
    private final MapperFacade mapperFacade;
    private final UserMapper userMapper;

    @Autowired
    public MyAccountImp(UserMapper userMapper, MapperFacade mapperFacade) {
        this.userMapper = userMapper;
        this.mapperFacade = mapperFacade;
    }


    @Override
    public Boolean comparePassword(Long id, String inputOldPassword) {
        User user = userMapper.selectById(id);
        String userOldPassword = user.getPassword();
        return new BCryptPasswordEncoder().matches(inputOldPassword, userOldPassword);
    }

    @Override
    public Boolean updatePassword(Long id, String newPassword) {
        User user = new User();
        user.setId(id);
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        int resultNum = userMapper.updateById(user);
        return resultNum > 0;
    }

}
