package server.controller.sysoption.account;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.CLogin;
import server.db.primary.model.sysoption.CLoginIplimit;
import server.db.primary.model.sysoption.CLoginResponsible;
import server.security.JavaJWT;
import server.service.FunAuthGroupService;
import server.service.FunCLoginIplimitService;
import server.service.FunCLoginResponsibleService;
import server.service.FunCLoginService;
import server.tool.Res;

import java.util.*;

//用户管理
@RequestMapping("/user")
@RestController("FunUserController")
public class UserController {

    @Autowired
    FunCLoginService funCLoginService;
    @Autowired
    FunAuthGroupService funAuthGroupService;
    @Autowired
    FunCLoginIplimitService funCLoginIplimitService;

    @PostMapping("/all")
    public Res all() {
        JSONObject jO = new JSONObject();
        jO.put("userList", funCLoginService.selectUser());
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO) {
        Long id = bodyJO.getLong("id");
        JSONObject jO = new JSONObject();
        if (id != null) {
            jO.put("user", funCLoginService.selectUserOneById(id));
        }
        jO.put("userGroupList", funAuthGroupService.selectGroupForList());
        return Res.successData(jO);
    }

    @PostMapping("/list")
    public Res list() {
        JSONObject jO = new JSONObject();
        jO.put("userList", funCLoginService.selectUserForList());
        return Res.successData(jO);
    }

    @PutMapping()
    public Res create(@RequestBody JSONObject bodyJO) {
        CLogin cLogin = bodyJO.getJSONObject("user").toJavaObject(CLogin.class);
        if (cLogin.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }
        try {
            cLogin.setCGid(cLogin.getAuthGroup().getId());
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("添加失败，所属组id有误");
        }
        if (funCLoginService.isUsernameExist(cLogin.getCLoginname())) {
            return Res.failureMsg("该账号已存在");
        }
        cLogin.setStime(new Date());
        cLogin.setRtime(new Date());
        if (funCLoginService.createUser(cLogin)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }

    @PatchMapping()
    public Res update(@RequestBody JSONObject bodyJO) {
        CLogin cLogin = bodyJO.getJSONObject("user").toJavaObject(CLogin.class);
        if (cLogin.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        try {
            cLogin.setCGid(cLogin.getAuthGroup().getId());
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("修改失败，所属组id有误");
        }
        if (funCLoginService.isUsernameExistExceptSelf(cLogin.getId(), cLogin.getCLoginname())) {
            return Res.failureMsg("该账号已存在");
        }
        cLogin.setRtime(new Date());
        if (funCLoginService.updateUser(cLogin)) {
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
        if (funCLoginService.deleteUserByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }

    @PostMapping("/ipLimit")
    public Res ipLimit(@RequestBody JSONObject bodyJO) {
        Long userId = bodyJO.getLong("cLoginId");
        JSONObject jO = new JSONObject();
        jO.put("ipLimitList", funCLoginIplimitService.selectIplimitByUserId(userId));
        return Res.successData(jO);
    }

    @PutMapping("/ipLimit")
    public Res createIpLimit(@RequestBody JSONObject bodyJO) {
        CLoginIplimit cLoginIplimit = bodyJO.getJSONObject("cLoginIplimit").toJavaObject(CLoginIplimit.class);
        if (cLoginIplimit == null) {
            return Res.failureMsg("添加失败,提交的信息为空");
        } else {
            if (cLoginIplimit.getCLoginId() == null) {
                return Res.failureMsg("添加失败，提交的用户id为空[cLoginId]");
            }
            if (cLoginIplimit.getIpAddr() == null) {
                return Res.failureMsg("添加失败，提交的ip为空[ipAddr]");
            }
        }
        if (funCLoginIplimitService.createCLoginIplimit(cLoginIplimit)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }

    @DeleteMapping("/ipLimit")
    public Res deleteIpLimit(@RequestBody JSONObject bodyJO) {
        if (!bodyJO.containsKey("idList")) {
            return Res.failureMsg("删除失败，缺少[idList]");
        }
        List<Long> idList = bodyJO.getJSONArray("idList").toJavaList(Long.class);
        if (idList.size() == 0) {
            return Res.failureMsg("删除失败，未选中对象");
        }
        if (funCLoginIplimitService.deleteCLoginIplimitByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }

    @Autowired
    FunCLoginResponsibleService funCLoginResponsibleService;

    @PostMapping("/responsible")
    public Res responsible(@RequestBody JSONObject bodyJO) {
        Long userId = bodyJO.getLong("cLoginId");
        JSONObject jO = new JSONObject();
        jO.put("responsibleList", funCLoginResponsibleService.selectResponsibleByUserId(userId));
        return Res.successData(jO);
    }

    @PutMapping("/responsible")
    public Res createResponsible(@RequestBody JSONObject bodyJO) {
        CLoginResponsible cLoginResponsible = bodyJO.getJSONObject("cLoginResponsible").toJavaObject(CLoginResponsible.class);
        if (cLoginResponsible == null) {
            return Res.failureMsg("添加失败,提交的信息为空");
        } else {
            if (cLoginResponsible.getCLoginId() == null) {
                return Res.failureMsg("添加失败，提交的用户id为空[cLoginId]");
            }
            if (cLoginResponsible.getName() == null) {
                return Res.failureMsg("添加失败，提交的姓名为空[name]");
            }
        }
        if (funCLoginResponsibleService.createCLoginResponsible(cLoginResponsible)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }

    @DeleteMapping("/responsible")
    public Res deleteResponsible(@RequestBody JSONObject bodyJO) {
        if (!bodyJO.containsKey("idList")) {
            return Res.failureMsg("删除失败，缺少[idList]");
        }
        List<Long> idList = bodyJO.getJSONArray("idList").toJavaList(Long.class);
        if (idList.size() == 0) {
            return Res.failureMsg("删除失败，未选中对象");
        }
        if (funCLoginResponsibleService.deleteCLoginResponsibleByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }

    @PostMapping("/responsible/list")
    public Res responsibleForList(@RequestHeader("Authorization") String token) {
        Long id = JavaJWT.getId(token);
        JSONObject jO = new JSONObject();
        List<Map> list = new ArrayList<>();
        List<CLoginResponsible> cLoginResponsibles = funCLoginResponsibleService.selectResponsibleByUserId(id);
        if (cLoginResponsibles != null) {
            for (CLoginResponsible cLoginResponsible : cLoginResponsibles) {
                Map<String, String> map = new HashMap<>();
                map.put("value", cLoginResponsible.getName());
                list.add(map);
            }
        }
        jO.put("responsibleList", list);
        return Res.successData(jO);
    }
}
