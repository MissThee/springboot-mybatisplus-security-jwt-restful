package server.controller.sysoption.well.meterStation;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.AreaInfo;
import server.db.primary.model.sysoption.StationInfo;
import server.db.primary.model.sysoption.StationInfoValve;
import server.service.FunAreaInfoService;
import server.service.FunStationInfoService;
import server.service.FunValveService;
import server.tool.Res;

import java.util.Date;
import java.util.List;

//阀组间信息管理
@RequestMapping("/valve")
@RestController("FunValveController")
public class ValveController {

    @Autowired
    FunStationInfoService funStationInfoService;
    @Autowired
    FunValveService funValveService;

    @PostMapping("/all")
    public Res all(@RequestBody JSONObject bodyJO) {
        Integer pageNum = bodyJO.getInteger("pageNum");
        Integer pageSize = bodyJO.getInteger("pageSize");
        Long searchFactoryId = bodyJO.getLong("searchFactoryId");
        Long searchAreaId = bodyJO.getLong("searchAreaId");
        Long searchStationId = bodyJO.getLong("searchStationId");

        Long searchMark = bodyJO.getLong("searchMark");
//        Long searchAutoMark = bodyJO.getLong("searchAutoMark");
        PageInfo pageInfo = funValveService.selectValvePaged(pageNum, pageSize, searchFactoryId, searchAreaId, searchStationId, searchMark);
        JSONObject jO = new JSONObject();
        jO.put("valveList", pageInfo.getList());
        jO.put("valveListTotal", pageInfo.getTotal());
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO) {
        Long id = bodyJO.getLong("id");
        JSONObject jO = new JSONObject();
        if (id != null) {
            jO.put("valve", funValveService.selectValveOneById(id));
        }
        return Res.successData(jO);
    }

//    @PostMapping("/list")
//    public Res list(@RequestBody(required = false) JSONObject bodyJO) {
//        Long areaId = null;
////        Long valveType = valveType;
//        if (bodyJO != null) {
//            areaId = bodyJO.getLong("areaId");
////            valveType = bodyJO.getLong("valveType");
//        }
//        JSONObject jO = new JSONObject();
//        jO.put("valveList", funValveService.selectValveForList(areaId));
//        return Res.successData(jO);
//    }

    @PutMapping()
    public Res create(@RequestBody JSONObject bodyJO) {
        StationInfoValve valveInfo = bodyJO.getJSONObject("valve").toJavaObject(StationInfoValve.class);
        if (valveInfo.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }
        try {
            transParam(valveInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("添加失败，所属计量站id有误");
        }
//        if (funValveService.isValveNameExist(valveInfo.getValveName())) {
//            return Res.failureMsg("该阀组间名称已存在");
//        }
        valveInfo.setStime(new Date());
        if (funValveService.createValve(valveInfo)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }

    private void transParam(StationInfoValve valveInfo) {
        StationInfo stationInfo = funStationInfoService.selectStationOneById(valveInfo.getStationId());
        valveInfo.setAreaId(stationInfo.getAreaId());
        valveInfo.setAreaName(stationInfo.getAreaName());
        valveInfo.setStationId(stationInfo.getId());
        valveInfo.setStationName(stationInfo.getStationName());
    }

    @PatchMapping()
    public Res update(@RequestBody JSONObject bodyJO) {
        StationInfoValve valveInfo = bodyJO.getJSONObject("valve").toJavaObject(StationInfoValve.class);
        if (valveInfo.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        try {
            transParam(valveInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("修改失败，所属计量站id有误");
        }
//        if (funValveService.isValveNameExistExceptSelf(valveInfo.getId(), valveInfo.getValveName())) {
//            return Res.failureMsg("该" + valveTypeName + "已存在");
//        }
        if (funValveService.updateValve(valveInfo)) {
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
        if (funValveService.deleteValveByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }
}
