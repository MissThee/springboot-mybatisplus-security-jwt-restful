package com.github.base.service.interf.login;

import com.github.base.dto.login.LoginDTO;
import org.springframework.stereotype.Service;

public interface LoginService  {
    LoginDTO selectUserByUsername(String username);
    LoginDTO selectUserById(Integer id);
}
