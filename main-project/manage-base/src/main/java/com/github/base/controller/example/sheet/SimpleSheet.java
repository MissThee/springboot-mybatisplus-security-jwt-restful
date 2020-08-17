package com.github.base.controller.example.sheet;

import com.alibaba.fastjson.JSONObject;
import com.github.missthee.tool.excel.exports.direct.ExcelExport;
import com.github.missthee.tool.excel.exports.direct.WorkBookVersion;
import com.github.missthee.tool.excel.template.SimpleCell;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/sheet/simple")
@RestController()
public class SimpleSheet {

    private Map<String, String> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", "");
        map.put("report_hour", "时间");
        map.put("report_loop_name", "掺水阀组");
        map.put("temp_out", "出站温度(℃)");
        map.put("press_out", "出站压力（MPa）");
        map.put("flow_inst_out", "瞬时流量（m³/h）");
        map.put("flow_totle_out", "流量计读数");
        map.put("water_val", "水量（m³）");
        map.put("temp_in", "入站温度(℃)");
        map.put("press_in", "入站压力（MPa）");
        map.put("flow_inst_in", "瞬时流量（m³/h）");
        map.put("flow_totle_in", "流量计读数");
        map.put("liquid_val", "液量（m³）");
        return map;
    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        List<SimpleSheetModel> reportData = getData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "简单报表（" + searchDateStr + "）";
        List<SimpleCell> extraHeaderCell = new ArrayList<>();
        extraHeaderCell.add(new SimpleCell("", 3));
        extraHeaderCell.add(new SimpleCell("掺水汇管（出站）", 5));
        extraHeaderCell.add(new SimpleCell("集油汇管（进站）", 5));
        Workbook wb = ExcelExport.buildWorkBookAndInsertData(WorkBookVersion.Excel97_2003, 0, 0, 0, fileName, getColumnMap(), true, reportData, true, extraHeaderCell);
        ExcelExport.exportToResponse(response, wb, fileName);
    }

    private List<SimpleSheetModel> getData() {
        List<SimpleSheetModel> list = new ArrayList<>();
        //        此处实际由数据库获取数据
        list.add(new SimpleSheetModel().setReportDate("00:00").setPress_in(11D).setPress_out(224D));
        list.add(new SimpleSheetModel().setReportDate("01:00"));
        list.add(new SimpleSheetModel().setReportDate("02:00").setPress_in(51D));
        list.add(new SimpleSheetModel().setReportDate("03:00").setPress_in(16D).setPress_out(224D));
        return list;
    }

    @Data
    @Accessors(chain = true)
    public static class SimpleSheetModel {
        private Long id;
        private String report_hour;//report_hour  时间
        private String report_loop_name;//report_loop_name  掺水阀组
        private Double temp_out;//temp_out  出站温度(℃)
        private Double press_out;//press_out  出站压力（MPa）
        private Double flow_inst_out;//flow_inst_out  瞬时流量（m³/h）
        private Double flow_totle_out;//flow_totle_out  流量计读数
        private Double water_val;//water_val  水量(m³)
        private Double temp_in;//temp_in  入站温度(℃)
        private Double press_in;//press_in  入站压力（MPa）
        private Double flow_inst_in;//flow_inst_in  瞬时流量（m³/h）
        private Double flow_totle_in;//flow_totle_in  流量计读数
        private Double liquid_val;//liquid_val  液量(m³)
        private String reportDate;
        private Long reportLoopId;
        private Long reportStationId;
        private String reportStationName;
        private Long areaId;
        private String areaName;
        private String remark;
        private Short mark;
        private Date stime;
    }
}
