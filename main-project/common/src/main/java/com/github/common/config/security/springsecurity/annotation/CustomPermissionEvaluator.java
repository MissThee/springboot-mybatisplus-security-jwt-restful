package com.github.common.config.security.springsecurity.annotation;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;


//自定义hasPermission表达式，此方法自己实现否则hasPermission表达式不可用，spring security默认不提供
//方法首个参数自动传入，注解中的参数由第二个开始对应
//使用方法为@PreAuthorize("hasPermission(#xxxXxx,'permissionValue')")，xxxXxx可为被注解的方法的参数,permissionValue为权限值字符串

//其实可以直接使用hasAuthority('permissionValue')的，个人比较喜欢hasPermission,hasRole的组合；同时也举个例子，如何扩展表达式。
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
