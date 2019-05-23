package com.github.missthee.controller.example;


import com.github.missthee.tool.Res;
import org.apache.shiro.authz.annotation.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
//权限访问测试
@RestController
@RequestMapping("/auth")
public class AuthController {

    //-----以下为权限测试，若需测试权限功能，需将本controller访问url先加入到shiro的检测路径中。-----
    @RequestMapping("/everyone")
    public Res everyone() {
        return Res.success("WebController：everyone");
    }

    @RequestMapping("/user")
    @RequiresUser//因为订制rememberMe功能，作用同@RequiresAuthentication
    public Res user() {
        return Res.success("WebController：user");
    }

    @RequestMapping("/guest")
    @RequiresGuest
    public Res guest() {
        return Res.success("WebController：guest");
    }

    @RequestMapping("/require_auth")
    @RequiresAuthentication
    public Res requireAuth() {
        return Res.success("WebController：You are authenticated");
    }

    @RequestMapping("/require_role1")
    @RequiresRoles({"role1"})
    public Res requireRole1() {
        return Res.success("WebController：You are visiting require_role12 [role1&role2]");
    }

    @RequestMapping("/require_role3")
    @RequiresRoles("role3")
    public Res requireRole3() {
        return Res.success("WebController：You are visiting require_role [role3]");
    }

    @RequestMapping("/require_role_permission")
    @RequiresPermissions({"admin:view"})
    public Res requireRolePermission() {
        return Res.success("WebController：You are visiting permission require admin:view");
    }

    @RequestMapping("/require_permission")
    @RequiresPermissions({"view", "edit"})
    public Res requirePermission() {
        return Res.success("WebController：You are visiting permission require edit,view");
    }

}
