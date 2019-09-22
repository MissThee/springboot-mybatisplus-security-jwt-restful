package com.github.missthee.config.security.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

//自定义hasPermission方法，此方法必须自己实现，默认不提供
//方法首个参数自动传入，注解中的参数由第二个开始对应
//使用方法为@PreAuthorize("hasRole('[ADMIN]')  or hasPermission(#xxxXxx,'experience')")，xxxXxx可为方法接收的参数
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object targetPermission) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(targetPermission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
