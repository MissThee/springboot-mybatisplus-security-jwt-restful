package com.github.base.service.interf.login;

import com.github.base.dto.login.AuthDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthInfoService {
    AuthDTO selectUserByUsername(String username);
    AuthDTO selectUserById(String id);
}
