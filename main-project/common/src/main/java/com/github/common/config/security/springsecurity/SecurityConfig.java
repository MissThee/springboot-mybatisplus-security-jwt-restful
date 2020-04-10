package com.github.common.config.security.springsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //csrf说明：在Security的默认拦截器里，默认会开启CSRF处理，判断请求是否携带了_csrf校验值，如果没有就拒绝访问。在请求为(GET|HEAD|TRACE|OPTIONS)时，则不会校验。
        http.csrf().disable(); //（必须）post可直接发送。因不再依赖Cookies传输身份信息，这里关闭csrf。
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//（非必须）完全禁用session。
        http.formLogin().disable();//（非必须）停用security的登录页。
        http.httpBasic().disable();//（非必须）停用security的弹窗登录。
    }
}
//关于@EnableGlobalMethodSecurity的参数作用：

//securedEnabled=true
//开启@Secured 注解过滤权限。如：@Secured({“ROLE_EMP”})、@Secured({“ROLE_EMP”, “ROLE_ROOT”})、@Secured({“ROLE_ROOT”})

//jsr250Enabled=true
//开启@RolesAllowed 注解过滤权限

//prePostEnabled=true
//使得表达式时间方法级别的安全性4个注解可用
//@PreAuthorize 在方法调用之前,基于表达式的计算结果来限制对方法的访问
//@PostAuthorize 允许方法调用,但是如果表达式计算结果为false,将抛出一个安全性异常
//@PostFilter 允许方法调用,但必须按照表达式来过滤方法的结果
//@PreFilter 允许方法调用,但必须在进入方法之前过滤输入值
