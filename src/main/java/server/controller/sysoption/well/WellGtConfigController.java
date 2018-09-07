package server.controller.sysoption.well;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.mapper.sysoption.FunWellInfoMapper;
import server.db.primary.model.sysoption.WellGtConfig;
import server.security.JavaJWT;
import server.service.FunWellGtConfigService;
import server.service.FunWellInfoService;
import server.tool.Res;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

//油井信息管理
@RequestMapping("/wellGtConfig")
@RestController("FunWellGtConfigController")
public class WellGtConfigController {

    @Autowired
    FunWellGtConfigService funWellGtConfigService;
    @Autowired
    FunWellInfoService funWellInfoService;

    @PostMapping("/all")
    public Res all(@RequestBody JSONObject bodyJO, @RequestHeader("Authorization") String token) {
        Integer pageNum = bodyJO.getInteger("pageNum");
        Integer pageSize = bodyJO.getInteger("pageSize");
        String searchWellName = bodyJO.getString("searchWellName");
        List<Long> areaIds = JavaJWT.getAreaIds(token);
        PageInfo pageInfo = funWellGtConfigService.selectWellGtConfigPaged(pageNum, pageSize, areaIds, searchWellName);
        JSONObject jO = new JSONObject();
        jO.put("wellGtConfigList", pageInfo.getList());
        jO.put("wellGtConfigListTotal", pageInfo.getTotal());
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO, @RequestHeader("Authorization") String token) {
        Long id = bodyJO.getLong("id");
        Boolean needWellList = false;
        if (bodyJO.containsKey("needWellList")) {
            needWellList = bodyJO.getBoolean("needWellList");
        }
        List<Long> areaIds = JavaJWT.getAreaIds(token);
        JSONObject jO = new JSONObject();
        if (id != null) {
            jO.put("wellGtConfig", funWellGtConfigService.selectWellGtConfigOneById(id));
        }
        if (needWellList) {
            jO.put("wellNoConfigList", funWellInfoService.selectWellForListNotInWellGtConfig(areaIds));
        }
        return Res.successData(jO);
    }

//    @PostMapping("/list")
//    public Res list() {
//        JSONObject jO = new JSONObject();
//        jO.put("wellGtConfigList", funWellGtConfigService.selectWellGtConfigForList());
//        return Res.successData(jO);
//    }

    @PutMapping()
    public Res create(@RequestBody JSONObject bodyJO) {
        WellGtConfig wellGtConfig = bodyJO.getJSONObject("wellGtConfig").toJavaObject(WellGtConfig.class);
        if (wellGtConfig.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }
        if (funWellGtConfigService.isWellGtConfigWellExist(wellGtConfig.getWellId())) {
            return Res.failureMsg("该井配置已存在");
        }
        wellGtConfig.setStime(new Date());
        Map<String, Object> map = funWellGtConfigService.createWellGtConfig(wellGtConfig);
        if (Boolean.getBoolean(map.get("result").toString())) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加" + map.get("msg").toString());
        }
    }


    @PatchMapping()
    public Res update(@RequestBody JSONObject bodyJO) {
        WellGtConfig wellGtConfig = bodyJO.getJSONObject("wellGtConfig").toJavaObject(WellGtConfig.class);
        if (wellGtConfig.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
//        if (funWellGtConfigService.isWellGtConfigWellExistExceptSelf(wellGtConfig.getId(), wellGtConfig.getWellId())) {
//            return Res.failureMsg("该井配置已存在");
//        }
        wellGtConfig.setWellId(null);
        wellGtConfig.setWellName(null);
        wellGtConfig.setWellNum(null);
        wellGtConfig.setAreaId(null);
        wellGtConfig.setAreaName(null);
        if (funWellGtConfigService.updateWellGtConfig(wellGtConfig)) {
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
        if (funWellGtConfigService.deleteWellGtConfigByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }
}
