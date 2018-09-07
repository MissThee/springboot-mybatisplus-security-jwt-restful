package server.controller.example;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.security.JavaJWT;
import server.tool.FileRec;
import server.tool.Res;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
//权限访问测试
@RestController
@RequestMapping("/test")
public class ExampleController {
    //获取当前用户相关信息。
    @PostMapping("infoByHeader")
    public Res getInfo(@RequestHeader("Authorization") String token) {
        Long id = JavaJWT.getId(token);                                     //当前用户的 用户id
        Long groupId = JavaJWT.getGroupId(token);                           //当前用户的 组群id
        Long objId = JavaJWT.getObjId(token);                               //当前用户的 对象id
        Long roleId = JavaJWT.getRoleId(token);                             //当前用户的 角色id
        List<Long> areaIds = JavaJWT.getAreaIds(token);                     //当前用户的 对象对应的工区,Id 和一个 -1 值（仅为auth_obj_station 表中的查询结果）
        Long ipLimitMark = JavaJWT.getIpLimitMark(token);                   //当前用户的 ip限制标志 0-无限制、1-限制登录、2-限制操作
//        Long unitId = JavaJWT.getUnitId(token);                             //当前用户 所属单位（厂\工区\站）id
//        String unitType = JavaJWT.getUnitType(token);                       //获取当前用户 所属单位类型:FACTORY-厂；AREA-工区；STATION-站
//        List<String> roleList=JavaJWT.getRoleList(token);                   //当前用户角色值（角色号）列表
//        List<String> permissionListList=JavaJWT.getPermissionList(token);   //当前用户权限值列表
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("groupId", groupId);
        map.put("objId", objId);
        map.put("roleId", roleId);
        map.put("areaIds", areaIds);
        map.put("ipLimitMark", ipLimitMark);
//        map.put("unitId", unitId);
//        map.put("unitType", unitType);
//        map.put("roleList", roleList);
//        map.put("permissionListList", permissionListList);
        return Res.successData(map);
    }

    //-----以下为权限测试，若需测试权限功能，需将本controller访问url先加入到shiro的检测路径中。-----
    @RequestMapping("/require_auth")
    @RequiresAuthentication
    public Res requireAuth() {
        return Res.successMsg("WebController：You are authenticated");
    }

    @RequestMapping("/require_role")
    @RequiresRoles("admin")
    public Res requireRole() {
        return Res.successMsg("WebController：You are visiting require_role");
    }

    @RequestMapping("/require_role_permission")
    @RequiresPermissions(value = {"admin:view"})
    public Res requireRolePermission() {
        return Res.successMsg("WebController：You are visiting permission require edit,view");
    }

    @RequestMapping("/require_permission")
    @RequiresPermissions(value = {"view", "edit"})
    public Res requirePermission() {
        return Res.successMsg("WebController：You are visiting permission require edit,view");
    }
    //-------------------------------------------------------------------------------------------------

    //上传文件示例
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject fileUpload(@RequestParam("file") MultipartFile file) {
        return FileRec.fileUpload(file, "uploadTest/image");
    }

}
