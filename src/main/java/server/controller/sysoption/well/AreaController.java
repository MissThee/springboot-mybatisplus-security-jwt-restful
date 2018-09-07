package server.controller.sysoption.well;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.AreaInfo;
import server.db.primary.model.sysoption.FactoryInfo;
import server.security.JavaJWT;
import server.service.FunAreaInfoService;
import server.service.FunFactoryInfoService;
import server.tool.Res;

import java.util.Date;
import java.util.List;

//工区信息管理
@RequestMapping("/area")
@RestController("FunAreaController")
public class AreaController {

    @Autowired
    FunAreaInfoService funAreaInfoService;
    @Autowired
    FunFactoryInfoService funFactoryInfoService;

    @PostMapping("/all")
    public Res all(@RequestBody JSONObject bodyJO, @RequestHeader("Authorization") String token) {
        Integer pageNum = bodyJO.getInteger("pageNum");
        Integer pageSize = bodyJO.getInteger("pageSize");
        List<Long> areaIds = JavaJWT. getAreaIds(token);
        JSONObject jO = new JSONObject();
        PageInfo pageInfo = funAreaInfoService.selectAreaPaged(pageNum, pageSize);
        jO.put("areaList", pageInfo.getList());
        jO.put("areaListTotal", pageInfo.getTotal());
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO) {
        Long id = bodyJO.getLong("id");
        JSONObject jO = new JSONObject();
        if (id != null) {
            jO.put("area", funAreaInfoService.selectAreaOneById(id));
        }
        jO.put("factoryList", funFactoryInfoService.selectFactoryForList("a11CodeFather"));
        return Res.successData(jO);
    }

    @PostMapping("/list")
    public Res list(@RequestBody(required = false) JSONObject bodyJO,@RequestHeader("Authorization") String token) {
        Long factoryId = null;
        if (bodyJO != null) {
            factoryId = bodyJO.getLong("factoryId");
        }
        List<Long> areaIds = JavaJWT. getAreaIds(token);
        JSONObject jO = new JSONObject();
        jO.put("areaList", funAreaInfoService.selectAreaForList(factoryId,areaIds, "a11CodeFather", "a11Code"));
        return Res.successData(jO);
    }

    @PutMapping()
    public Res create(@RequestBody JSONObject bodyJO) {
        AreaInfo areaInfo = bodyJO.getJSONObject("area").toJavaObject(AreaInfo.class);
        if (areaInfo.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }
        try {
            FactoryInfo factoryInfo = funFactoryInfoService.selectFactoryById(areaInfo.getCoId());
            areaInfo.setCoId(factoryInfo.getId());
            areaInfo.setCoName(factoryInfo.getName());
            areaInfo.setA11CodeFather(factoryInfo.getA11CodeFather());
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("添加失败，所属厂信息有误");
        }
        if (funAreaInfoService.isAreaNameExist(areaInfo.getAreaName())) {
            return Res.failureMsg("该工区已存在");
        }
        areaInfo.setStime(new Date());
        if (funAreaInfoService.createArea(areaInfo)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }

    @PatchMapping()
    public Res update(@RequestBody JSONObject bodyJO) {
        AreaInfo areaInfo = bodyJO.getJSONObject("area").toJavaObject(AreaInfo.class);
        if (areaInfo.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        try {
            FactoryInfo factoryInfo = funFactoryInfoService.selectFactoryById(areaInfo.getCoId());
            areaInfo.setCoId(factoryInfo.getId());
            areaInfo.setCoName(factoryInfo.getName());
            areaInfo.setA11CodeFather(factoryInfo.getA11CodeFather());
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("修改失败，所属厂信息有误");
        }
        if (funAreaInfoService.isAreaNameExistExceptSelf(areaInfo.getId(), areaInfo.getAreaName())) {
            return Res.failureMsg("该工区已存在");
        }
        if (funAreaInfoService.updateArea(areaInfo)) {
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
        if (funAreaInfoService.deleteAreaByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }
}
