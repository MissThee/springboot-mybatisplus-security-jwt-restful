package server.controller.login;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import server.db.primary.dto.login.LoginDTO;
import server.config.security.JavaJWT;
import server.service.LoginService;
import server.tool.Res;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "登录")
@RestController("FunLoginController")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @ApiOperation(value = "登录", notes = "账号密码登录，获取token及用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "string", example = "admin"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", example = "123")
    })
    @PostMapping("/login")
    public Res login(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestBody LoginModel loginModel) {
        String username = loginModel.getUsername();
        String password = loginModel.getPassword();
        if (StringUtils.isEmpty(username)) {
            return Res.failure("用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            return Res.failure("密码不能为空");
        }
        Boolean sevenDaysLogin = loginModel.sevenDaysLogin;
        LoginDTO loginDTO;
        try {
            loginDTO = loginService.selectUserByUsername(username, password);
        } catch (UnauthenticatedException e) {
            return Res.failure(e.getMessage());
        }
        //添加token
        httpServletResponse.setHeader("Authorization", JavaJWT.createToken(loginDTO.getId(), sevenDaysLogin ? 7 : 1));

        return Res.success(new loginRes(loginDTO), "登录成功");
    }

    @Data
    @ApiModel(description = "接收登录参数")
    private static class LoginModel {
        @ApiModelProperty(value = "账号", required = true, example = "admin")
        private String username;
        @ApiModelProperty(value = "密码", required = true, example = "123")
        private String password;
        private Boolean sevenDaysLogin = false;
    }

    @Data
    @AllArgsConstructor
    private class loginRes {
        private LoginDTO user;
    }
}

