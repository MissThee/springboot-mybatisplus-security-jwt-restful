package server.controller.reportform;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.reportform.ReportDataOilWell_Res;
import server.security.JavaJWT;
import server.service.FunReportConfStationService;
import server.service.FunReportDataOilWellService;
import server.tool.ExcelUtils;
import server.tool.Res;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/reportForm/oilWell")
@RestController("FunOilWellReportController")
public class OilWellReportController {

    @Autowired
    FunReportConfStationService funReportConfStationService;
    @Autowired
    FunReportDataOilWellService funReportDataOilWellService;

    private Map<String, String> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("id", "");
            put("name", "掺水阀组");
            put("ring_seq", "环序号");
            put("well_name", "井号");
            put("pump_dia", "泵径");
            put("pump_len", "泵深");
            put("well_stroke", "冲程");
            put("jig_frequency", "冲次");
            put("stroke_frequency", "上报冲程/冲次");
            put("prod_liquid", "日产液");
            put("prod_water_cut", "含水");
            put("i_upper", "上行电流");
            put("i_lower", "下行电流");
            put("event_well_switch", "开关井事件");
            put("remark", "备注");
        }};
    }

    @PostMapping("/treeMenu")
    public Res treeMenu(@RequestHeader(value = "Authorization", required = false) String token) {
        List<Long> areaIds = token == null ? null : JavaJWT.getAreaIds(token);
        JSONObject jO = new JSONObject();
        jO.put("treeMenu", funReportConfStationService.selectTreeByAreaIds(areaIds));
        return Res.successData(jO);
    }

    @PostMapping()
    public Res dataTable(@RequestBody JSONObject bodyJO) {
        List<ReportDataOilWell_Res> reportDataOilWell = getData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", reportDataOilWell);
        return Res.successData(jO);
    }

    @PatchMapping()
    public Res dataEdit(@RequestBody JSONObject bodyJO) {
        ReportDataOilWell_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportDataOilWell_Res.class);
        if (reportData.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        if (funReportDataOilWellService.updateData(reportData)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        List<ReportDataOilWell_Res> reportData = getData(bodyJO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "油井报表数据（" + searchDateStr + "）";
        String title = fileName + "油井日报表数据";
        ExcelUtils.export(response, fileName, title, getColumnMap(), true, reportData, true);
    }

    private List<ReportDataOilWell_Res> getData(JSONObject bodyJO) {
        Long areaId = bodyJO.getLong("areaId");
        Long stationId = bodyJO.getLong("stationId");
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        return funReportDataOilWellService.selectReportData(areaId, stationId, searchDateStr, getColumnMap());
    }
}
