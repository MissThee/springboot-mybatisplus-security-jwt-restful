package server.service;

import server.db.primary.model.User;

public interface UserService {
    User selectUserByUsername(String username);

    User selectUserById(Long id);
}
