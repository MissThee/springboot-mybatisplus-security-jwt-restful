package com.github.missthee.service.interf.basic;

import com.github.missthee.db.primary.model.basic.User;
import java.util.List;

public interface UserService {
    User selectOneById(Integer userId);

    User selectOneByUsername(String username);

    int insertOne(User user);

    List<User> selectByNickname(String username);
}
