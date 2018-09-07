package server.controller.sysoption.account;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.mapper.sysoption.FunAuthObjMapper;
import server.db.primary.model.sysoption.AuthGroup;
import server.db.primary.model.sysoption.CLogin;
import server.service.FunAuthGroupService;
import server.service.FunAuthObjService;
import server.service.FunAuthObjStationService;
import server.service.FunAuthRoleService;
import server.tool.Res;

import java.util.List;

@RequestMapping("/group")
@RestController("FunGroupController")
public class GroupController {

    @Autowired
    FunAuthGroupService funAuthGroupService;
    @Autowired
    FunAuthRoleService funAuthRoleService;
    @Autowired
    FunAuthObjService funAuthObjService;
    @PostMapping("/all")
    public Res all() {
        JSONObject jO = new JSONObject();
        jO.put("groupList", funAuthGroupService.selectGroupTable());
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO) {
        JSONObject jO = new JSONObject();
        Long id = bodyJO.getLong("id");
        if(id!=null) {
            jO.put("group", funAuthGroupService.selectGroupTableOneById(id));
        }
        jO.put("groupRoleList", funAuthRoleService.selectRoleForList());
        jO.put("groupObjList", funAuthObjService.selectObjForList());

        return Res.successData(jO);
    }

    @PostMapping("/list")
    public Res list() {
        JSONObject jO = new JSONObject();
        jO.put("groupList", funAuthGroupService.selectGroupForList());
        return Res.successData(jO);
    }

    @PutMapping()
    public Res create(@RequestBody JSONObject bodyJO) {
        AuthGroup authGroup = bodyJO.getJSONObject("group").toJavaObject(AuthGroup.class);
        if (authGroup.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }
        if (funAuthGroupService.isGroupNoExist(authGroup.getGroupNo())) {
            return Res.failureMsg("该组群代号已存在");
        }
        if (funAuthGroupService.isGroupNameExist(authGroup.getGroupName())) {
            return Res.failureMsg("该组群名称已存在");
        }
        if (authGroup.getAuthObj() != null) {
            authGroup.setGroupObjId(authGroup.getAuthObj().getId());
        }
        if (authGroup.getAuthRole() != null) {
            authGroup.setGroupRoleId(authGroup.getAuthRole().getId());
        }
        if (funAuthGroupService.createGroup(authGroup)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }

    @PatchMapping()
    public Res update(@RequestBody JSONObject bodyJO) {
        AuthGroup authGroup = bodyJO.getJSONObject("group").toJavaObject(AuthGroup.class);
        if (authGroup.getId() == null) {
            return Res.failureMsg("修改失败，无[id]");
        }
        if (funAuthGroupService.isGroupNoExistExceptSelf(authGroup.getId(), authGroup.getGroupNo())) {
            return Res.failureMsg("该组群代号已存在");
        }
        if (funAuthGroupService.isGroupNameExistExceptSelf(authGroup.getId(), authGroup.getGroupName())) {
            return Res.failureMsg("该组群名称已存在");
        }
        if (authGroup.getAuthObj() != null) {
            authGroup.setGroupObjId(authGroup.getAuthObj().getId());
        }
        if (authGroup.getAuthRole() != null) {
            authGroup.setGroupRoleId(authGroup.getAuthRole().getId());
        }
        if (funAuthGroupService.updateGroup(authGroup)) {
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
        if (funAuthGroupService.deleteGroupByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }
}
