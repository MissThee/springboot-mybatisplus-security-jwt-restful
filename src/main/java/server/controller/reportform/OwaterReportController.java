package server.controller.reportform;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.reportform.ReportDataOwaterWells_Res;
import server.db.primary.model.reportform.ReportDataOwaterWells_Res;
import server.security.JavaJWT;
import server.service.FunReportConfStationService;
import server.service.FunReportDataOilWellService;
import server.service.FunReportDataOwaterWellsService;
import server.tool.ExcelUtils;
import server.tool.Res;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/reportForm/owater")
@RestController("FunOwaterReportController")
public class OwaterReportController {

    @Autowired
    FunReportConfStationService funReportConfStationService;
    @Autowired
    FunReportDataOwaterWellsService funReportDataOwaterWellsService;

    private Map<String, String> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("id", "");
            put("loop_name", "掺水阀组");
            put("report_well_num", "环序");
            put("report_well_name", "环号");
            put("press_oil_08", "08:00");
            put("press_oil_10", "10:00");
            put("press_oil_12", "12:00");
            put("press_oil_14", "14:00");
            put("press_oil_16", "16:00");
            put("press_oil_18", "18:00");
            put("press_oil_20", "20:00");
            put("press_oil_22", "22:00");
            put("press_oil_00", "00:00");
            put("press_oil_02", "02:00");
            put("press_oil_04", "04:00");
            put("press_oil_06", "06:00");
        }};
    }

    @PostMapping("/treeMenu")
    public Res treeMenu(@RequestHeader(value = "Authorization",required = false) String token) {
        List<Long> areaIds = token == null ? null : JavaJWT.getAreaIds(token);
        JSONObject jO = new JSONObject();
        jO.put("treeMenu", funReportConfStationService.selectTreeByAreaIdsAndIdRange(areaIds, 2L));
        return Res.successData(jO);
    }

    @PostMapping()
    public Res dataTable(@RequestBody JSONObject bodyJO) {
        List<ReportDataOwaterWells_Res> reportDataOilWell = getData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", reportDataOilWell);
        return Res.successData(jO);
    }

    @PatchMapping()
    public Res dataEdit(@RequestBody JSONObject bodyJO) {
        ReportDataOwaterWells_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportDataOwaterWells_Res.class);
        if (reportData.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        if (funReportDataOwaterWellsService.updateData(reportData)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        List<ReportDataOwaterWells_Res> reportData = getData(bodyJO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "掺水回压报表数据（" + searchDateStr + "）";
        ExcelUtils.export(response, fileName, fileName, getColumnMap(), true, reportData, true);
    }

    private List<ReportDataOwaterWells_Res> getData(JSONObject bodyJO) {
        Long stationId = bodyJO.getLong("stationId");
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        return funReportDataOwaterWellsService.selectReportData(stationId, searchDateStr, getColumnMap());
    }
}
