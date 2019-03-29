package com.github.missthee.service.imp.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.missthee.config.security.jwt.UserInfoForJWT;
import com.github.missthee.db.dto.manage.usercontroller.InsertOneReq;
import com.github.missthee.db.dto.manage.usercontroller.SelectListReq;
import com.github.missthee.db.dto.manage.usercontroller.UpdateOneReq;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.github.missthee.db.mapper.primary.manage.UserMapper;
import com.github.missthee.db.po.primary.manage.User;
import com.github.missthee.service.interf.manage.UserService;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserImp implements UserService, UserInfoForJWT {
    private final UserMapper userMapper;
    private final MapperFacade mapperFacade;

    @Autowired
    public UserImp(UserMapper userMapper, MapperFacade mapperFacade) {
        this.userMapper = userMapper;
        this.mapperFacade = mapperFacade;
    }

    @Override
    public Long insertOne(InsertOneReq insertOneReq) {
        User user = mapperFacade.map(insertOneReq, User.class);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public Boolean deleteOne(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean updateOne(UpdateOneReq updateOneReq) {
        User user = mapperFacade.map(updateOneReq, User.class);
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        return userMapper.updateById(user) > 0;
    }

    @Override
    public User selectOne(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> selectList(SelectListReq selectListReq){
        return userMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public Boolean deleteOnePhysical(Long id) {
        return userMapper.deleteById(id)>0;
    }

    @Override
    public String getSecret(Object userId) {
        String userIdStr = String.valueOf(userId);
        if ("null".equals(userId) || "".equals(userIdStr)) {
            throw new BadCredentialsException("empty userId, when get secret");
        }
        User user = userMapper.selectById(String.valueOf(userId));
        if (user == null) {
            throw new BadCredentialsException("can't find user, when get secret");
        }
        return user.getPassword();

    }
}
