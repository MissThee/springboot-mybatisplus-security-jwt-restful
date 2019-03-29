package com.github.missthee.service.interf.login;

import com.github.missthee.db.bo.login.LoginBO;

public interface LoginService  {
    LoginBO selectUserByUsername(String username);
    LoginBO selectUserById(Integer id);
}
