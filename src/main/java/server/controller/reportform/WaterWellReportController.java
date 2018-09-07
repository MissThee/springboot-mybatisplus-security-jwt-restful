package server.controller.reportform;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.reportform.ReportDataOilWell_Res;
import server.db.primary.model.reportform.ReportDataWaterWell_Res;
import server.security.JavaJWT;
import server.service.FunReportConfWaterStationService;
import server.service.FunReportDataWaterWellService;
import server.tool.ExcelUtils;
import server.tool.Res;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/reportForm/waterWell")
@RestController("FunWaterWellReportController")
public class WaterWellReportController {

    @Autowired
    FunReportConfWaterStationService funReportConfWaterStationService;
    @Autowired
    FunReportDataWaterWellService funReportDataWaterWellService;

    private Map<String, String> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("id", "");
            put("station_name", "配水站");
            put("water_well_name", "井号");
            put("injection", "配注");
            put("press_trunk", "干压（MPa）");
            put("press_oil", "油压（MPa）");
            put("flow_yes", "昨日累计流量（m³）");
            put("flow_today", "累计流量（m³）");
            put("flow_water_day", "日注水量");
            put("event_well_switch", "开关井事件");
            put("remark", "备注信息");
        }};
    }

    @PostMapping("/treeMenu")
    public Res treeMenu(@RequestHeader(value = "Authorization", required = false) String token) {
        List<Long> areaIds = token == null ? null : JavaJWT.getAreaIds(token);
        JSONObject jO = new JSONObject();
        jO.put("treeMenu", funReportConfWaterStationService.selectTreeByAreaIds(areaIds));
        return Res.successData(jO);
    }


    @PostMapping()
    public Res dataTable(@RequestBody JSONObject bodyJO) {
        List<ReportDataWaterWell_Res> reportDataWaterWell = getData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", reportDataWaterWell);
        return Res.successData(jO);
    }

    @PatchMapping()
    public Res dataEdit(@RequestBody JSONObject bodyJO) {
        ReportDataWaterWell_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportDataWaterWell_Res.class);
        if (reportData.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        if (funReportDataWaterWellService.updateData(reportData)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        List<ReportDataWaterWell_Res> reportData = getData(bodyJO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "水井报表数据（" + searchDateStr + "）";
        String title = fileName + "水井日报表数据";
        ExcelUtils.export(response, fileName, title, getColumnMap(), true, reportData, true);
    }

    private List<ReportDataWaterWell_Res> getData(JSONObject bodyJO) {
        Long areaId = bodyJO.getLong("areaId");
        Long stationId = bodyJO.getLong("stationId");
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        return funReportDataWaterWellService.selectReportData(stationId, areaId, searchDateStr, getColumnMap());
    }
}
