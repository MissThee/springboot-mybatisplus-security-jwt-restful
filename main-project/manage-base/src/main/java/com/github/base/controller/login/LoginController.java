package com.github.base.controller.login;

import com.github.base.dto.login.AuthDTO;
import com.github.base.service.interf.login.AuthInfoService;
import com.github.base.service.interf.manage.UserService;
import com.github.base.vo.login.LoginVO;
import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.tool.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "登录")
@ApiSort(999)
@RestController
public class LoginController {

    private final AuthInfoService authInfoService;
    private final JavaJWT javaJWT;

    @Autowired
    public LoginController(AuthInfoService authInfoService, JavaJWT javaJWT, UserService userService) {
        this.authInfoService = authInfoService;
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
        AuthDTO authDTO = authInfoService.selectUserByUsername(loginModel.getUsername());
        if (authDTO == null) {
            return Res.failure("无此账号");
        }
        if (!authDTO.getIsEnable()) {
            return Res.failure("账号已停用");
        }
        if (!new BCryptPasswordEncoder().matches(loginModel.getPassword(), authDTO.getPassword())) {
            return Res.failure("密码错误");
        }
        httpServletResponse.setHeader(JavaJWT.JWT_TOKEN_KEY, javaJWT.createToken(authDTO.getId(), loginModel.getIsLongLogin() ? 7 : 2));  //添加token
        return Res.success(new LoginVO.LoginRes().setUser(authDTO), "登录成功");
    }


    @ApiOperation(value = "获取用户信息", notes = "token获取用户信息")
    @PostMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public Res<LoginVO.LoginRes> info(HttpServletRequest httpServletRequest) {
        String id = JavaJWT.getId();
        AuthDTO authDTO = authInfoService.selectUserById(id);
        if (authDTO == null) {
            throw new BadCredentialsException("user not exist, when get user info");
        }
        return Res.success(new LoginVO.LoginRes().setUser(authDTO), "登录成功");
    }

}

