package server.controller.sysoption.well.meterStation;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.AreaInfo;
import server.db.primary.model.sysoption.StationInfo;
import server.db.primary.model.sysoption.StationInfoWaterwell;
import server.service.FunAreaInfoService;
import server.service.FunStationInfoService;
import server.service.FunWaterwellService;
import server.tool.Res;

import java.util.Date;
import java.util.List;

//注水井信息管理
@RequestMapping("/waterwell")
@RestController("FunWaterwellController")
public class WaterwellController {

    @Autowired
    FunStationInfoService funStationInfoService;
    @Autowired
    FunWaterwellService funWaterwellService;

    @PostMapping("/all")
    public Res all(@RequestBody JSONObject bodyJO) {
        Integer pageNum = bodyJO.getInteger("pageNum");
        Integer pageSize = bodyJO.getInteger("pageSize");
        Long searchStationId = bodyJO.getLong("searchStationId");
        Long searchMark = bodyJO.getLong("searchMark");
        PageInfo pageInfo = funWaterwellService.selectWaterwellPaged(pageNum, pageSize, searchStationId, searchMark);
        JSONObject jO = new JSONObject();
        jO.put("waterwellList", pageInfo.getList());
        jO.put("waterwellListTotal", pageInfo.getTotal());
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO) {
        Long id = bodyJO.getLong("id");
        JSONObject jO = new JSONObject();
        if (id != null) {
            jO.put("waterwell", funWaterwellService.selectWaterwellOneById(id));
        }
        return Res.successData(jO);
    }

//    @PostMapping("/list")
//    public Res list(@RequestBody(required = false) JSONObject bodyJO) {
//        Long areaId = null;
////        Long waterwellType = waterwellType;
//        if (bodyJO != null) {
//            areaId = bodyJO.getLong("areaId");
////            waterwellType = bodyJO.getLong("waterwellType");
//        }
//        JSONObject jO = new JSONObject();
//        jO.put("waterwellList", funWaterwellInfoService.selectWaterwellForList(areaId, waterwellType));
//        return Res.successData(jO);
//    }

    @PutMapping()
    public Res create(@RequestBody JSONObject bodyJO) {
        StationInfoWaterwell waterwell = bodyJO.getJSONObject("waterwell").toJavaObject(StationInfoWaterwell.class);
        if (waterwell.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }
        try {
            transParam(waterwell);
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("添加失败，所属计量站id有误");
        }
        if (funWaterwellService.isWaterwellNameExist(waterwell.getWellWaterName())) {
            return Res.failureMsg("该注水井名称已存在");
        }
        waterwell.setStime(new Date());
        if (funWaterwellService.createWaterwell(waterwell)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }

    private void transParam(StationInfoWaterwell waterwell) {
        Long id = waterwell.getStationId();
        StationInfo stationInfo = funStationInfoService.selectStationOneById(id);
        waterwell.setAreaId(stationInfo.getAreaId());
        waterwell.setAreaName(stationInfo.getAreaName());
        waterwell.setStationId(stationInfo.getId());
        waterwell.setStationName(stationInfo.getStationName());
    }

    @PatchMapping()
    public Res update(@RequestBody JSONObject bodyJO) {
        StationInfoWaterwell waterwell = bodyJO.getJSONObject("waterwell").toJavaObject(StationInfoWaterwell.class);
        if (waterwell.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        try {
            transParam(waterwell);
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("修改失败，所属计量站id有误");
        }
        if (funWaterwellService.isWaterwellNameExistExceptSelf(waterwell.getId(), waterwell.getWellWaterName())) {
            return Res.failureMsg("该注水井名称已存在");
        }
        if (funWaterwellService.updateWaterwell(waterwell)) {
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
        if (funWaterwellService.deleteWaterwellByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }
}
