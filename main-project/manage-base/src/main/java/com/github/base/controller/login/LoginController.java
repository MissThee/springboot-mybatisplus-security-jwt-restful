package com.github.base.controller.login;

import com.github.base.vo.login.LoginVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.github.common.dto.login.LoginDTO;
import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.service.interf.manage.UserService;
import com.github.common.service.interf.login.LoginService;
import com.github.common.tool.Res;

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
    public Res<LoginVO.LoginRes> login(HttpServletResponse httpServletResponse, @RequestBody LoginVO.LoginReq loginModel) throws Exception {
        if (StringUtils.isEmpty(loginModel.getUsername())) {
            return Res.failure("用户名不能为空");
        }
        if (StringUtils.isEmpty(loginModel.getPassword())) {
            return Res.failure("密码不能为空");
        }
        LoginDTO loginDTO = loginService.selectUserByUsername(loginModel.getUsername());
        if (loginDTO == null) {
            return Res.failure("无此账号");
        }
        if (!loginDTO.getIsEnable()) {
            return Res.failure("账号已停用");
        }
        if (!new BCryptPasswordEncoder().matches(loginModel.getPassword(), loginDTO.getPassword())) {
            return Res.failure("密码错误");
        }
        httpServletResponse.setHeader("Authorization", javaJWT.createToken(loginDTO.getId(), loginModel.getIsLongLogin() ? 7 : 2));  //添加token
        return Res.success(new LoginVO.LoginRes().setUser(loginDTO), "登录成功");
    }


    @ApiOperation(value = "获取用户信息", notes = "token获取用户信息")
    @PostMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public Res<LoginVO.LoginRes> info(HttpServletRequest httpServletRequest) {
        String id = javaJWT.getId(httpServletRequest);
        LoginDTO loginDTO = loginService.selectUserById(Integer.parseInt(id));
        if (loginDTO == null) {
            throw new BadCredentialsException("user not exist, when get user info");
        }
        return Res.success(new LoginVO.LoginRes().setUser(loginDTO), "登录成功");
    }

}

