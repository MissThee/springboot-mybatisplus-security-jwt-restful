package com.github.missthee.service.imp.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.missthee.db.primary.mapper.basic.UserMapper;
import com.github.missthee.db.primary.model.basic.User;
import com.github.missthee.service.interf.basic.UserService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserImp implements UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserImp(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User selectOneById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public User selectOneByUsername(String username) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo(User.USERNAME, username);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public int insertOne(User user) {
        return userMapper.insertSelective(user);
    }

    @Override
    public List<User> selectByNickname(String username) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo(User.NICKNAME, username);
        return userMapper.selectByExample(example);
    }


}
