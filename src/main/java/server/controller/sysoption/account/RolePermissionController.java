package server.controller.sysoption.account;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.AuthRole;
import server.db.primary.model.sysoption.AuthRolePermission;
import server.service.FunAuthRolePermissionService;
import server.tool.Res;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//权限管理
@RequestMapping("/permission")
@RestController("FunRolePermissionController")
public class RolePermissionController {

    @Autowired
    FunAuthRolePermissionService funAuthRolePermissionService;

    @PostMapping()
    public Res select(@RequestBody JSONObject bodyJO) {
        Long roleId = bodyJO.getLong("roleId");
        JSONObject jO = new JSONObject();
        List<AuthRolePermission> authRolePermissionList = funAuthRolePermissionService.selectPermissionByRoleId(roleId);
        List<String> permissionIdList = new ArrayList<>();
        for (AuthRolePermission authRolePermission : authRolePermissionList) {
            permissionIdList.add(authRolePermission.getPermissionId());
        }
        jO.put("permissionIdList", permissionIdList);
        return Res.successData(jO);
    }

    @Transactional
    @PatchMapping()
    @PutMapping()
    public Res edit(@RequestBody JSONObject bodyJO) {
        List<String> permissionIdList = bodyJO.getJSONArray("permissionIdList").toJavaList(String.class);
        Long roleId = bodyJO.getLong("roleId");
        if (StringUtils.isEmpty(roleId)) {
            return Res.failureMsg("编辑失败,[roleId]为空");
        }
        try {
            funAuthRolePermissionService.deleteRolePermissionByRoleId(roleId);
            for (String permissionId : permissionIdList) {
                AuthRolePermission authRolePermission = new AuthRolePermission();
                authRolePermission.setRoleId(roleId);
                authRolePermission.setPermissionId(permissionId);
                funAuthRolePermissionService.createRolePermission(authRolePermission);
            }
            return Res.successMsg("编辑成功");
        } catch (Exception e) {
            return Res.failureMsg("编辑失败");
        }
    }

}
