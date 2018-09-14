package server.controller.sheet;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sheet.ReportDataOwaterLoop_Day_Res;
import server.service.SheetSimpleService;
import server.tool.ExcelUtils;
import server.tool.Res;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/sheet/simple")
@RestController()
public class SimpleSheet {
    @Autowired
    SheetSimpleService sheetSimpleService;

    private Map<String, String> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("id", "");
            put("report_hour", "时间");
            put("report_loop_name", "掺水阀组");
            put("temp_out", "出站温度(℃)");
            put("press_out", "出站压力（MPa）");
            put("flow_inst_out", "瞬时流量（m³/h）");
            put("flow_totle_out", "流量计读数");
            put("water_val", "水量(m³)");
            put("temp_in", "入站温度(℃)");
            put("press_in", "入站压力（MPa）");
            put("flow_inst_in", "瞬时流量（m³/h）");
            put("flow_totle_in", "流量计读数");
            put("liquid_val", "液量(m³)");
        }};
    }

    @PostMapping()
    public Res dataTable(@RequestBody JSONObject bodyJO) {
        List<ReportDataOwaterLoop_Day_Res> reportDataOilWell = getData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", reportDataOilWell);
        return Res.successData(jO);
    }

    @PatchMapping()
    public Res dataEdit(@RequestBody JSONObject bodyJO) {
        ReportDataOwaterLoop_Day_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportDataOwaterLoop_Day_Res.class);
        if (reportData.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        if (sheetSimpleService.updateOwaterDayData(reportData)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        List<ReportDataOwaterLoop_Day_Res> reportData = getData(bodyJO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "简单报表（" + searchDateStr + "）";
        List<ExcelUtils.HeaderCell> extraHeaderCell = new ArrayList<ExcelUtils.HeaderCell>() {{
            add(new ExcelUtils.HeaderCell("", 3));
            add(new ExcelUtils.HeaderCell("掺水汇管（出站）", 5));
            add(new ExcelUtils.HeaderCell("集油汇管（进站）", 5));
        }};
        ExcelUtils.export(response, fileName, fileName, getColumnMap(), true, reportData, true, extraHeaderCell);
    }

    private List<ReportDataOwaterLoop_Day_Res> getData(JSONObject bodyJO) {
        Long stationId = bodyJO.getLong("stationId");
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);

        searchDateStr = "2018-08-24";
        stationId = 1L;

        return sheetSimpleService.selectReportDataDay(stationId, searchDateStr, getColumnMap());
    }
}
