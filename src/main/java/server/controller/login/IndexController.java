package server.controller.login;

import io.swagger.annotations.*;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.User;
import server.security.JavaJWT;
import server.service.UserService;
import server.tool.Res;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "获取用户信息")
@RestController("FunIndexController")
public class IndexController {
    @Autowired
    UserService userService;

    @ApiOperation(value = "获取用户信息", notes = "通过token获取用户信息，用于token有效期内的自动登录")
    @PostMapping("/getUserInfo")
    public Res<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") @ApiParam(value = "http header头中的token值") String token) {
        Long id = JavaJWT.getId(token);
        User user = userService.selectUserById(id);
        if (user == null) {
            throw new UnauthenticatedException("身份失效，需要登录");
        }
        Map<String, Object> map = new HashMap<>();
        user.setPassword(null);
        map.put("user", user);
        return Res.success(map, "登录成功");
    }
}
