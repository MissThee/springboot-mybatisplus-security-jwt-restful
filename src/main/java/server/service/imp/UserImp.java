package server.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.UserMapper;
import server.db.primary.model.User;
import server.service.UserService;
@Service
public class UserImp implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public User selectUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    public User selectUserById(Long id) {
        return userMapper.selectUserById(id);
    }
}
