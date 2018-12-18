package server.service;

import server.db.primary.model.basic.User;

public interface UserService {
    User selectOneById(Integer userId);
}
