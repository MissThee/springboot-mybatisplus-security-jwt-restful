package com.github.common.config.security.security.filter;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserInfoForSecurity {
    UserDetails loadUserById(Object id) throws BadCredentialsException;
}
