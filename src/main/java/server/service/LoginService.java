package server.service;

import server.db.primary.dto.login.LoginDTO;

public interface LoginService {
    LoginDTO selectUserByUsername(String username, String password);

    LoginDTO selectUserById(Integer id);
}
