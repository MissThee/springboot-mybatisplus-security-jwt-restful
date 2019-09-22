package com.github.missthee.service.interf.login;

import com.github.missthee.dto.login.LoginDTO;

public interface LoginService {
    LoginDTO selectUserByUsername(String username);

    LoginDTO selectUserById(Integer id);
}
