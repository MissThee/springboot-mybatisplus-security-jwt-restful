package server.controller.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.config.security.JavaJWT;
import server.db.primary.model.basic.User;
import server.service.interf.basic.UserService;
import server.tool.TreeData;
import server.tool.FileRec;
import server.tool.Res;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
//权限访问测试
@RestController
@RequestMapping("/test")
public class ExampleController {
    private final FileRec fileRec;
    private final TreeData buildTree;
    private final UserService userService;
    private final JavaJWT javaJWT;

    @Autowired
    public ExampleController(FileRec fileRec, TreeData buildTree, UserService userService, JavaJWT javaJWT) {
        this.fileRec = fileRec;
        this.buildTree = buildTree;
        this.userService = userService;
        this.javaJWT = javaJWT;
    }

    //获取当前用户相关信息。
    @PostMapping("infoByHeader")
    public Res getInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        String userIdByToken = javaJWT.getId(token);//通过token解析获得
        Object userIdBySubject = SecurityUtils.getSubject().getPrincipal();//通过shiro的subject获得
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("userIdByToken", userIdByToken);
            put("userIdBySubject", userIdBySubject);
        }};
        return Res.success(map);
    }

    @PostMapping("addUser")
    public Res addUser(@RequestBody JSONObject bJO) {
        String username = bJO.getString("username");
        String password = bJO.getString("password");
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        User user = new User();
        user.setUsername(username);
        String md5Password = new Md5Hash(password, salt, 3).toString();
        user.setPassword(md5Password);
        user.setNickname(username);
        user.setSalt(salt);
        int result = userService.insertOne(user);
        JSONObject jO = new JSONObject() {{
            put("result", result);
            put("user", user);
        }};
        if (result > 0) {
            return Res.success(jO, "成功");
        } else {
            return Res.failure(jO, "失败");
        }
    }

    @RequestMapping("tree")
    public Res getTree(@RequestParam("c") Boolean compareSelfId, @RequestParam("r") Integer rootId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<TreeItem> li = new ArrayList<TreeItem>() {{
            add(new TreeItem(1, "name1", null));
            add(new TreeItem(2, "name2", 1));
            add(new TreeItem(3, "name3", 2));
            add(new TreeItem(4, "name4", 2));
            add(new TreeItem(5, "name5", null));
            add(new TreeItem(6, "name6", 2));
        }};
        JSONArray objects = buildTree.tree(li, rootId, compareSelfId == null ? false : compareSelfId, new HashMap<>());
        return Res.success(objects);
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    private class TreeItem {
        private Integer id;
        private String name;
        private Integer parentId;
    }

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

    @RequestMapping("/require_role12")
    @RequiresRoles({"role1","role2"})
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
    //-------------------------------------------------------------------------------------------------

    //上传文件示例
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject fileUpload(@RequestParam("file") MultipartFile file) {
        return fileRec.fileUpload(file, "uploadTest/image");
    }

    //上传文件示例1
    @PostMapping(value = "/upload1")
    public JSONObject fileUpload1(MultipartFile file, String tip) {
        return fileRec.fileUpload(file, "uploadTest/image");
    }

}
