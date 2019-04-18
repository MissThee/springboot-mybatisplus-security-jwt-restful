package com.github.base.service.interf.login;

import com.github.base.dto.login.LoginDTO;

public interface LoginService  {
    LoginDTO selectUserByUsername(String username);
    LoginDTO selectUserById(Integer id);
}
