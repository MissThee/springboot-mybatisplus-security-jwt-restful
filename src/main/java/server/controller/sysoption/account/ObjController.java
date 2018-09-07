package server.controller.sysoption.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.AreaInfo;
import server.db.primary.model.sysoption.AuthObj;
import server.service.FunAuthObjService;
import server.service.FunAuthObjService;
import server.service.FunAuthObjStationService;
import server.service.FunFactoryInfoService;
import server.tool.Res;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//对象管理
@RequestMapping("/obj")
@RestController("FunObjController")
public class ObjController {

    @Autowired
    FunAuthObjService funAuthObjService;
    @Autowired
    FunFactoryInfoService funFactoryInfoService;


    @PostMapping("/all")
    public Res all() {
        JSONObject jO = new JSONObject();
        List<AuthObj> authObjList = funAuthObjService.selectObj();
        jO.put("objList", authObjList);
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO) {
        Long id = bodyJO.getLong("id");
        JSONObject jO = new JSONObject();
        if (id != null) {
            AuthObj authObj = funAuthObjService.selectObjById(id);
            List<Long> areaIdList = new ArrayList<>();
            for (AreaInfo areaInfo : authObj.getAreaInfoList()) {
                areaIdList.add(areaInfo.getId());
            }
            authObj.setAreaInfoList(null);
            jO.put("obj", authObj);
            jO.put("objAreaIdList", areaIdList);
        }
        jO.put("logoList", funAuthObjService.getLogoList());
        jO.put("unitTree", funFactoryInfoService.getUnitTree());
        return Res.successData(jO);
    }

    @PostMapping("/list")
    public Res list() {
        JSONObject jO = new JSONObject();
        jO.put("objList", funAuthObjService.selectObjForList());
        return Res.successData(jO);
    }


    @PutMapping()
    public Res create(@RequestBody JSONObject bodyJO) {
        AuthObj authObj = bodyJO.getJSONObject("obj").toJavaObject(AuthObj.class);
        List<Long> objAreaIdList = bodyJO.getJSONArray("objAreaIdList").toJavaList(Long.class);

        if (authObj.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }
        if (funAuthObjService.isObjExist(authObj.getObjNo())) {
            return Res.failureMsg("该对象值代号已存在");
        }
        if (funAuthObjService.isNameExist(authObj.getObjName())) {
            return Res.failureMsg("该对象名称已存在");
        }
        try {
            authObj.setLogoPng(authObj.getLogoPng().substring(authObj.getLogoPng().lastIndexOf('/')));
        } catch (Exception e) {
            authObj.setLogoPng("/default_petrochinalogo.png");
        }
        authObj.setRtime(new Date());
        authObj.setStime(new Date());
        if (funAuthObjService.createObj(authObj, objAreaIdList)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }

    }

    @PatchMapping()
    public Res update(@RequestBody JSONObject bodyJO) {
        AuthObj authObj = bodyJO.getJSONObject("obj").toJavaObject(AuthObj.class);
        List<Long> objAreaIdList = bodyJO.getJSONArray("objAreaIdList").toJavaList(Long.class);
        if (authObj.getId() == null) {
            return Res.failureMsg("修改失败，无[id]");
        }
        if (funAuthObjService.isObjExistExceptSelf(authObj.getId(), authObj.getObjNo())) {
            return Res.failureMsg("该对象值代号已存在");
        }
        if (funAuthObjService.isNameExistExceptSelf(authObj.getId(), authObj.getObjName())) {
            return Res.failureMsg("该对象名称已存在");
        }
        try {
            authObj.setLogoPng(authObj.getLogoPng().substring(authObj.getLogoPng().lastIndexOf('/')));
        } catch (Exception e) {
            authObj.setLogoPng("/default_petrochinalogo.png");
        }
        authObj.setRtime(new Date());
        if (funAuthObjService.updateObj(authObj, objAreaIdList)) {
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
        if (funAuthObjService.deleteObjByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }
}
