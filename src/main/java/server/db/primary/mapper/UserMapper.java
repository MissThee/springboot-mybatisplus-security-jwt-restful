package server.db.primary.mapper;

import org.springframework.stereotype.Component;
import server.db.common.CommonMapper;
import server.db.primary.model.User;
@Component
public interface UserMapper extends CommonMapper<User> {

    User selectUserByUsername(String username);

    User selectUserById(Long id);
}