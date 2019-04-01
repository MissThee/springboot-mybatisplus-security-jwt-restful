package com.github.missthee.controller.login;

import com.github.missthee.db.dto.login.LoginReqDTO;
import com.github.missthee.db.dto.login.LoginResDTO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.github.missthee.db.bo.login.LoginBO;
import com.github.missthee.config.security.jwt.JavaJWT;
import com.github.missthee.service.interf.manage.UserService;
import com.github.missthee.service.interf.login.LoginService;
import com.github.missthee.tool.Res;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "登录")
@RestController
public class LoginController {

    private final LoginService loginService;
    private final JavaJWT javaJWT;

    @Autowired
    public LoginController(LoginService loginService, JavaJWT javaJWT, UserService userService) {
        this.loginService = loginService;
        this.javaJWT = javaJWT;
    }

    @ApiOperation(value = "登录", notes = "账号密码登录(返回的token在header中的authorization)")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "string", example = "admin"),
//            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", example = "123")
//    })
    @PostMapping("/login")
    public Res<LoginResDTO> login(HttpServletResponse httpServletResponse, @RequestBody LoginReqDTO loginModel) throws Exception {
        if (StringUtils.isEmpty(loginModel.getUsername())) {
            return Res.failure("用户名不能为空");
        }
        if (StringUtils.isEmpty(loginModel.getPassword())) {
            return Res.failure("密码不能为空");
        }
        LoginBO loginDTO = loginService.selectUserByUsername(loginModel.getUsername());
        if (loginDTO == null) {
            return Res.failure("无此账号");
        }
        if (!new BCryptPasswordEncoder().matches(loginModel.getPassword(), loginDTO.getPassword())) {
            return Res.failure("密码错误");
        }
        httpServletResponse.setHeader("Authorization", javaJWT.createToken(loginDTO.getId(), loginModel.getIsLongLogin() ? 7 : 2));  //添加token
        return Res.success(new LoginResDTO(loginDTO), "登录成功");
    }


    @ApiOperation(value = "获取用户信息", notes = "token获取用户信息")
    @PostMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public Res<LoginResDTO> info( HttpServletRequest httpServletRequest) {
        String id = javaJWT.getId(httpServletRequest);
        LoginBO loginDTO = loginService.selectUserById(Integer.parseInt(id));
        if (loginDTO == null) {
            throw new BadCredentialsException("user not exist, when get user info");
        }
        return Res.success(new LoginResDTO(loginDTO), "登录成功");
    }

}

