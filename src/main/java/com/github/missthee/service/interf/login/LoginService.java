package server.service.interf.login;

import server.db.primary.dto.login.LoginDTO;

public interface LoginService {
    LoginDTO selectUserByUsername(String username);

    LoginDTO selectUserById(Integer id);
}
