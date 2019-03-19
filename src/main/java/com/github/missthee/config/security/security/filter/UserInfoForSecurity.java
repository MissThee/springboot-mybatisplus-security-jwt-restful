package com.github.missthee.config.security.security.filter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public interface UserInfoForSecurity {
    UserDetails loadUserById(Object id) throws UsernameNotFoundException;
}
