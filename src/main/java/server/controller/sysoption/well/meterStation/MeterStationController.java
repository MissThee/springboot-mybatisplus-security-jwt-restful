package server.controller.sysoption.well.meterStation;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sysoption.AreaInfo;
import server.db.primary.model.sysoption.CLoginIplimit;
import server.db.primary.model.sysoption.StationInfo;
import server.db.primary.model.sysoption.StationInfoVideo;
import server.security.JavaJWT;
import server.service.FunAreaInfoService;
import server.service.FunStationInfoService;
import server.service.FunStationInfoVideoService;
import server.tool.Res;

import java.util.Date;
import java.util.List;

//计量站信息管理
@RequestMapping("/meterStation")
@RestController("FunMeterStationController")
public class MeterStationController {

    @Autowired
    FunStationInfoService funStationInfoService;
    @Autowired
    FunAreaInfoService funAreaInfoService;
    @Autowired
    FunStationInfoVideoService funStationInfoVideoService;

    private final Long stationType = 1L;//0-联合站；1-计量站
    private final String stationTypeName = "计量站";

    @PostMapping("/all")
    public Res all(@RequestBody JSONObject bodyJO, @RequestHeader("Authorization") String token) {
        Integer pageNum = bodyJO.getInteger("pageNum");
        Integer pageSize = bodyJO.getInteger("pageSize");
        Long searchFactoryId = bodyJO.getLong("searchFactoryId");
        Long searchAreaId = bodyJO.getLong("searchAreaId");
        Long searchMark = bodyJO.getLong("searchMark");
        Long searchAutoMark = bodyJO.getLong("searchAutoMark");
        List<Long> areaIds = JavaJWT.getAreaIds(token);
        PageInfo pageInfo = funStationInfoService.selectStationPaged(pageNum, pageSize, areaIds, stationType, searchFactoryId, searchAreaId, searchMark, searchAutoMark);
        JSONObject jO = new JSONObject();
        jO.put("stationList", pageInfo.getList());
        jO.put("stationListTotal", pageInfo.getTotal());
        return Res.successData(jO);
    }

    @PostMapping("/one")
    public Res one(@RequestBody JSONObject bodyJO) {
        Long id = bodyJO.getLong("id");
        JSONObject jO = new JSONObject();
        if (id != null) {
            jO.put("station", funStationInfoService.selectStationOneById(id));
        }
        return Res.successData(jO);
    }

    @PostMapping("/list")
    public Res list(@RequestBody(required = false) JSONObject bodyJO, @RequestHeader("Authorization") String token) {
        Long areaId = null;
//        Long stationType = stationType;
        if (bodyJO != null) {
            areaId = bodyJO.getLong("areaId");
//            stationType = bodyJO.getLong("stationType");
        }
        List<Long> areaIds = JavaJWT.getAreaIds(token);
        JSONObject jO = new JSONObject();
        jO.put("stationList", funStationInfoService.selectStationForList(areaId, areaIds, stationType));
        return Res.successData(jO);
    }

    @PutMapping()
    public Res create(@RequestBody JSONObject bodyJO) {
        StationInfo stationInfo = bodyJO.getJSONObject("station").toJavaObject(StationInfo.class);
        if (stationInfo.getId() != null) {
            return Res.failureMsg("添加失败，不可提交[id]于创建表中");
        }
        try {
            transParam(stationInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("添加失败，所属井区id有误");
        }
        if (funStationInfoService.isStationNameExist(stationInfo.getStationName())) {
            return Res.failureMsg("该" + stationTypeName + "已存在");
        }
        stationInfo.setStime(new Date());
        if (funStationInfoService.createStation(stationInfo)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }

    private void transParam(StationInfo stationInfo) {
        AreaInfo areaInfo = funAreaInfoService.selectAreaById(stationInfo.getAreaId());
        stationInfo.setCoId(areaInfo.getCoId());
        stationInfo.setCoName(areaInfo.getCoName());
        stationInfo.setAreaId(areaInfo.getId());
        stationInfo.setAreaName(areaInfo.getAreaName());
        stationInfo.setA11CodeFather(areaInfo.getA11CodeFather() + areaInfo.getA11Code());
        stationInfo.setStationType(stationType);
        stationInfo.setStationTypeName(stationTypeName);
    }

    @PatchMapping()
    public Res update(@RequestBody JSONObject bodyJO) {
        StationInfo stationInfo = bodyJO.getJSONObject("station").toJavaObject(StationInfo.class);
        if (stationInfo.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        try {
            transParam(stationInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Res.failureMsg("修改失败，所属井区id有误");
        }
        if (funStationInfoService.isStationNameExistExceptSelf(stationInfo.getId(), stationInfo.getStationName())) {
            return Res.failureMsg("该" + stationTypeName + "已存在");
        }
        if (funStationInfoService.updateStation(stationInfo)) {
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
        if (funStationInfoService.deleteStationByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }


    @PostMapping("/videoIp")
    public Res videoIp(@RequestBody JSONObject bodyJO) {
        Long stationId = bodyJO.getLong("stationId");
        JSONObject jO = new JSONObject();
        jO.put("videoIpList", funStationInfoVideoService.selectVideoIpByStationId(stationId));
        return Res.successData(jO);
    }

    @PutMapping("/videoIp")
    public Res createVideoIp(@RequestBody JSONObject bodyJO) {
        StationInfoVideo stationInfoVideo = bodyJO.getJSONObject("videoIp").toJavaObject(StationInfoVideo.class);
        if (stationInfoVideo == null) {
            return Res.failureMsg("添加失败,提交的信息为空");
        } else {
            if (stationInfoVideo.getStationId() == null) {
                return Res.failureMsg("添加失败，提交的stationId为空[stationId]");
            }
            if (StringUtils.isEmpty(stationInfoVideo.getVIpaddr())) {
                return Res.failureMsg("添加失败，提交的ip为空[vIpaddr]");
            }
        }
        if (funStationInfoVideoService.createVideoIp(stationInfoVideo)) {
            return Res.successMsg("添加成功");
        } else {
            return Res.failureMsg("添加失败");
        }
    }

    @DeleteMapping("/videoIp")
    public Res deleteVideoIp(@RequestBody JSONObject bodyJO) {
        if (!bodyJO.containsKey("idList")) {
            return Res.failureMsg("删除失败，缺少[idList]");
        }
        List<Long> idList = bodyJO.getJSONArray("idList").toJavaList(Long.class);
        if (idList.size() == 0) {
            return Res.failureMsg("删除失败，未选中对象");
        }
        if (funStationInfoVideoService.deleteVideoIpByIdList(idList)) {
            return Res.successMsg("删除成功");
        } else {
            return Res.failureMsg("删除失败");
        }
    }
}
