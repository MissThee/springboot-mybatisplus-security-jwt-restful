package server.controller.sysoption.well;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.AreaInfo;
import server.db.primary.model.sysoption.StationInfo;
import server.db.primary.model.sysoption.WellInfo;
import server.security.JavaJWT;
import server.service.FunStationInfoService;
import server.service.FunWellInfoService;
import server.tool.Res;

import java.util.Date;
import java.util.List;

//油井信息管理
@RequestMapping("/wellInfo")
@RestController("FunWellInfoController")
public class WellInfoController {

    @Autowired
    FunWellInfoService funWellInfoService;
    @Autowired
    FunStationInfoService funStationInfoService;

    @PostMapping("/all")
    public Res all(@RequestBody JSONObject bodyJO, @RequestHeader("Authorization") String token) {
        Integer pageNum = bodyJO.getInteger("pageNum");
        Integer pageSize = bodyJO.getInteger("pageSize");
        Long searchFactoryId = bodyJO.getLong("searchFactoryId");
        Long searchAreaId = bodyJO.getLong("searchAreaId");
        Long searchStationId = bodyJO.getLong("searchStationId");
        Long searchMark = bodyJO.getLong("searchMark");
        Long searchAutoMark = bodyJO.getLong("searchAutoMark");
        Date searchStartDate = bodyJO.getDate("searchStartDate");
        Date searchEndDate = bodyJO.getDate("searchEndDate");
        Long searchNetType = bodyJO.getLong("searchNetType");
        List<Long> areaIds = JavaJWT. getAreaIds(token);
        PageInfo pageInfo = funWellInfoService.selectWellPaged(pageNum, pageSize,areaIds, searchFactoryId, searchAreaId, searchStationId, searchMark, searchAutoMark, searchStartDate, searchEndDate, searchNetType);
        JSONObject jO = new JSONObject();
        jO.put("wellList", pageInfo.getList());
        jO.put("wellListTotal", pageInfo.getTotal());
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO) {
        Long id = bodyJO.getLong("id");
        JSONObject jO = new JSONObject();
        if (id != null) {
            jO.put("well", funWellInfoService.selectWellOneById(id));
        }
        return Res.successData(jO);
    }

//    @PostMapping("/list")
//    public Res list() {
//        JSONObject jO = new JSONObject();
//        jO.put("wellList", funWellInfoService.selectWellForList());
//        return Res.successData(jO);
//    }

    @PutMapping()
    public Res create(@RequestBody JSONObject bodyJO) {
        WellInfo wellInfo = bodyJO.getJSONObject("well").toJavaObject(WellInfo.class);
        if (wellInfo.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }
        try {
            transParam(wellInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("添加失败，所属站id有误");
        }
        if (funWellInfoService.isWellNameExist(wellInfo.getWellName())) {
            return Res.failureMsg("该井号已存在");
        }
        wellInfo.setStime(new Date());
        if (funWellInfoService.createWell(wellInfo)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }

    private void transParam(WellInfo wellInfo) {
        StationInfo stationInfo = funStationInfoService.selectStationOneById(wellInfo.getStationId());
        wellInfo.setAreaId(stationInfo.getAreaId());
        wellInfo.setAreaName(stationInfo.getAreaName());
        wellInfo.setStationId(stationInfo.getId());
        wellInfo.setStationName(stationInfo.getStationName());
    }

    @PatchMapping()
    public Res update(@RequestBody JSONObject bodyJO) {
        WellInfo wellInfo = bodyJO.getJSONObject("well").toJavaObject(WellInfo.class);
        if (wellInfo.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        try {
            transParam(wellInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("修改失败，所属站id有误");
        }
        if (funWellInfoService.isWellNameExistExceptSelf(wellInfo.getId(), wellInfo.getWellName())) {
            return Res.failureMsg("该井号已存在");
        }
        if (funWellInfoService.updateWell(wellInfo)) {
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
        if (funWellInfoService.deleteWellByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }
}
