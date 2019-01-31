package server.service.interf.login;

import server.db.primary.model.basic.User;

public interface UserService {
    User selectOneById(Integer userId);
}
