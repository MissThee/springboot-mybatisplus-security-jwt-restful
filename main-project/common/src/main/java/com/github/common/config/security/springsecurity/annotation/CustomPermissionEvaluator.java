package com.github.common.config.security.springsecurity.annotation;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

//自定义hasPermission方法，此方法必须自己实现，spring security默认不提供
//方法首个参数自动传入，注解中的参数由第二个开始对应
//使用方法为@PreAuthorize("hasPermission(#xxxXxx,'experience')")，xxxXxx可为被注解的方法的参数
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object argsObj, Object targetPermission) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(targetPermission)) {
                return true;
            }
        }
        return false;
    }

    //这个实现没使用，上一个就够用了，这个直接返回false。
    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
