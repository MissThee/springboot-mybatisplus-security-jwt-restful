package server.controller.login;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.dto.login.LoginDTO;
import server.config.security.JavaJWT;
import server.service.LoginService;
import server.tool.Res;

@Api(tags = "获取用户信息")
@RestController("FunIndexController")
public class IndexController {
    private final LoginService loginService;

    @Autowired
    public IndexController(LoginService loginService) {
        this.loginService = loginService;
    }

    @ApiOperation(value = "获取用户信息", notes = "通过token获取用户信息，用于token有效期内的自动登录")
    @PostMapping("/info")
    @RequiresAuthentication
    public Res<loginRes> info(@RequestHeader(value = "Authorization", required = false) String token) {
        String id = JavaJWT.getId(token);
        LoginDTO loginDTO = loginService.selectUserById(Integer.parseInt(id));

        return Res.success(new loginRes(loginDTO), "登录成功");
    }

    @Data
    @AllArgsConstructor
    private class loginRes {
        private LoginDTO user;
    }
}
