package server.controller.example;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.config.security.JavaJWT;
import server.tool.FileRec;
import server.tool.Res;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

@ApiIgnore
//权限访问测试
@RestController
@RequestMapping("/test")
public class ExampleController {


    //获取当前用户相关信息。
    @PostMapping("infoByHeader")
    public Res getInfo(@RequestHeader("Authorization") String token) {
        String id = JavaJWT.getId(token);
//        List roleList = JavaJWT.getRoleList(token);
//        List permissionList = JavaJWT.getPermissionList(token);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
//        map.put("roleList", roleList);
//        map.put("permissionList", permissionList);
        return Res.success(map);
    }

    //-----以下为权限测试，若需测试权限功能，需将本controller访问url先加入到shiro的检测路径中。-----
    @RequestMapping("/everyone")
    public Res everyone() {
        return Res.success("WebController：everyone");
    }

    @RequestMapping("/require_auth")
    @RequiresAuthentication
    public Res requireAuth() {
        return Res.success("WebController：You are authenticated");
    }

    @RequestMapping("/require_role1")
    @RequiresRoles("role1")
    public Res requireRole1() {
        return Res.success("WebController：You are visiting require_role [role1]");
    }

    @RequestMapping("/require_role3")
    @RequiresRoles("role3")
    public Res requireRole3() {
        return Res.success("WebController：You are visiting require_role [role3]");
    }

    @RequestMapping("/require_role_permission")
    @RequiresPermissions(value = {"admin:view"})
    public Res requireRolePermission() {
        return Res.success("WebController：You are visiting permission require edit,view");
    }

    @RequestMapping("/require_permission")
    @RequiresPermissions(value = {"view", "edit"})
    public Res requirePermission() {
        return Res.success("WebController：You are visiting permission require edit,view");
    }
    //-------------------------------------------------------------------------------------------------

    //上传文件示例
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject fileUpload(@RequestParam("file") MultipartFile file) {
        return FileRec.fileUpload(file, "uploadTest/image");
    }

}
