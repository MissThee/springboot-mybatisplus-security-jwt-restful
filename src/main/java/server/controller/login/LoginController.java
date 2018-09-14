package server.controller.login;

import io.swagger.annotations.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.Permission;
import server.db.primary.model.Role;
import server.db.primary.model.User;
import server.security.JavaJWT;
import server.service.UserService;
import server.tool.Res;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Api(tags = "登录")
@RestController("FunLoginController")
public class LoginController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "登录", notes = "账号密码登录，获取token及用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "string", example = "admin"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", example = "123")
    })
    @PostMapping("/login")
    public Res login(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestBody LoginModel loginModel) {
        String username = loginModel.getUsername();
        String password = loginModel.getPassword();
        User user = userService.selectUserByUsername(username);
        //登录密码校验
        if (user == null) {
            return Res.failureMsg("无此用户");
        } else if (!user.getPassword().equals(password)) {
            return Res.failureMsg("密码错误");
        }
        Map<String, List<String>> userInfoMap = getUserInfo(user);
        //添加token
        httpServletResponse.setHeader("Authorization", JavaJWT.createToken(user.getId(), userInfoMap.get("roleList"), userInfoMap.get("permissionList"), loginModel.sevenDaysLogin ? 7 : 1));
        Map<String, Object> map = new HashMap<>();
        user.setPassword(null);
        map.put("user", user);
        return Res.success(map, "登录成功");
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

    private Map<String, List<String>> getUserInfo(User user) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> roleList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();
        for (Role role : user.getRoleList()) {
            roleList.add(role.getRole());
            for (Permission permission : role.getPermissionList()) {
                permissionList.add(permission.getPermission());
            }
        }
        map.put("roleList", roleList);
        map.put("permissionList", permissionList);
        return map;
    }
}

