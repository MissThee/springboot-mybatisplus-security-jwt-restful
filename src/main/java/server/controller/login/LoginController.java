package server.controller.login;

import io.swagger.annotations.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.AuthRolePermission;
import server.db.primary.model.sysoption.CLogin;
import server.db.primary.model.sysoption.CLoginIplimit;
import server.security.JavaJWT;
import server.service.FunCLoginIplimitService;
import server.service.FunCLoginService;
import server.tool.Res;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Api(tags = "登录")
@RestController("FunLoginController")
public class LoginController {

    @Autowired
    FunCLoginService funCLoginService;
    @Autowired
    FunCLoginIplimitService funCLoginIplimitService;
    @Value("${custom-config.logo-png-web-path}")
    private String logoWebPath;

    @ApiOperation(value = "登录", notes = "账号密码登录，获取token及用户信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username1", value = "账号", required = true, dataType = "string"),
//            @ApiImplicitParam(name = "password12", value = "密码", required = true, dataType = "string")
//    })
    @PostMapping("/login")
    public Res login(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestBody LoginModel loginModel) {
        String username = loginModel.getUsername();
        String password = loginModel.getPassword();
        CLogin cLogin = funCLoginService.selectUserByUsername(username);
        //登录密码校验
        if (cLogin == null) {
            return Res.failureMsg("无此用户");
        } else if (!cLogin.getCLoginpwd().equals(password)) {
            return Res.failureMsg("密码错误");
        }
        //登录ip限制检测
        if (cLogin.getIpLimitMark() == 1L || cLogin.getIpLimitMark() == 2L) {
            boolean isInLimit = false;
            String userIp = httpServletRequest.getRemoteAddr();
            List<CLoginIplimit> iplimitList = funCLoginIplimitService.selectIplimitByUserId(cLogin.getId());
            for (CLoginIplimit cLoginIplimit : iplimitList) {
                if (userIp.equals(cLoginIplimit.getIpAddr().trim())) {
                    isInLimit = true;
                }
            }
            if (!isInLimit) {
                return Res.failureMsg("登录失败，该用户允许登录的ip中不包含当前ip");
            }
        }
        //添加token
        if (loginModel.sevenDaysLogin) {
            httpServletResponse.setHeader("Authorization", JavaJWT.createToken(cLogin, 7));
        } else {
            httpServletResponse.setHeader("Authorization", JavaJWT.createToken(cLogin));
        }
        Map<String, Object> map = new HashMap<>();
        cLogin.setCLoginpwd(null);

        try {
            List<String> permissionIdList = new ArrayList<>();
            List<AuthRolePermission> authRolePermissionList = cLogin.getAuthGroup().getAuthRole().getPermissionList();
            for (AuthRolePermission anAuthRolePermissionList : authRolePermissionList) {
                permissionIdList.add(anAuthRolePermissionList.getPermissionId());
            }
            cLogin.setPermissionIdList(permissionIdList);
        } catch (Exception e) {
            cLogin.setPermissionIdList(new ArrayList<>());
        }

        try {
            cLogin.getAuthGroup().getAuthObj().setLogoPng(logoWebPath + cLogin.getAuthGroup().getAuthObj().getLogoPng().substring(cLogin.getAuthGroup().getAuthObj().getLogoPng().lastIndexOf('/')));
        } catch (Exception e) {
            cLogin.getAuthGroup().getAuthObj().setLogoPng("/default_petrochinalogo.png");
        }

        map.put("user", cLogin);

        return Res.success(map, "登录成功");
    }

    @Data
    @ApiModel(description = "接收登录参数")
    private static class LoginModel {
        @ApiModelProperty(value = "账号", required = true, example = "admin")
        private String username;
        @ApiModelProperty(value = "密码", required = true, example = "123456")
        private String password;
        private Boolean sevenDaysLogin = false;
    }

}

