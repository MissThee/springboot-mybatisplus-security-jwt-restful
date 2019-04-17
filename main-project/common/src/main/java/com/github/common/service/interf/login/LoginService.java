package com.github.common.service.interf.login;

import com.github.common.dto.login.LoginDTO;

public interface LoginService  {
    LoginDTO selectUserByUsername(String username);
    LoginDTO selectUserById(Integer id);
}
