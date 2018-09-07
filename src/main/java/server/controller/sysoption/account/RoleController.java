package server.controller.sysoption.account;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.mapper.sysoption.FunAuthRoleMapper;
import server.db.primary.model.sysoption.AuthRole;
import server.service.FunAuthRoleService;
import server.tool.Res;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//角色管理
@RequestMapping("/role")
@RestController("FunRoleController")
public class RoleController {

    @Autowired
    FunAuthRoleService funAuthRoleService;

    @PostMapping("/all")
    public Res all() {
        JSONObject jO = new JSONObject();
        jO.put("roleList", funAuthRoleService.selectRole());
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO) {
        Long id = bodyJO.getLong("id");
        JSONObject jO = new JSONObject();
        if(id!=null) {
            jO.put("role", funAuthRoleService.selectRoleById(id));
        }
        return Res.successData(jO);
    }

    @PostMapping("/list")
    public Res list() {
        JSONObject jO = new JSONObject();
        jO.put("roleList", funAuthRoleService.selectRoleForList());
        return Res.successData(jO);
    }

    @PutMapping()
    public Res create(@RequestBody JSONObject bodyJO) {
        AuthRole authRole = bodyJO.getJSONObject("role").toJavaObject(AuthRole.class);
        if (authRole.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }
        if (funAuthRoleService.isRoleExist(authRole.getRoleNo())) {
            return Res.failureMsg("该角色值代号已存在");
        }
        if (funAuthRoleService.isNameExist(authRole.getRoleName())) {
            return Res.failureMsg("该角色名称已存在");
        }
        authRole.setRtime(new Date());
        authRole.setStime(new Date());
        if (funAuthRoleService.createRole(authRole)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }

    @PatchMapping()
    public Res update(@RequestBody JSONObject bodyJO) {
        AuthRole authRole = bodyJO.getJSONObject("role").toJavaObject(AuthRole.class);
        if (authRole.getId() == null) {
            return Res.failureMsg("修改失败，无[id]");
        }
        if (funAuthRoleService.isRoleExistExceptSelf(authRole.getId(), authRole.getRoleNo())) {
            return Res.failureMsg("该角色值代号已存在");
        }
        if (funAuthRoleService.isNameExistExceptSelf(authRole.getId(), authRole.getRoleName())) {
            return Res.failureMsg("该角色名称已存在");
        }
        authRole.setRtime(new Date());
        if (funAuthRoleService.updateRole(authRole)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    @DeleteMapping()
    public Res delete(@RequestBody JSONObject bodyJO) {
        if (!bodyJO.containsKey("idList")) {
            return Res.failureMsg("删除失败，缺少[idList]");
        }
        List<Long> idList = bodyJO.getJSONArray("idList").toJavaList(Long.class);
        if (idList.size() == 0) {
            return Res.failureMsg("删除失败，未选中对象");
        }
        if (funAuthRoleService.deleteRoleByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }
}
