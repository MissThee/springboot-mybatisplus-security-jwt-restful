package server.controller.sheet;

import com.alibaba.fastjson.JSONObject;
import com.github.missthee.tool.excel.exports.direct.ExcelExport;
import com.github.missthee.tool.excel.exports.direct.WorkBookVersion;
import com.github.missthee.tool.excel.template.SimpleCell;
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
        return new LinkedHashMap<String, String>() {{
            put("id", "");
            put("report_hour", "时间");
            put("report_loop_name", "掺水阀组");
            put("temp_out", "出站温度(℃)");
            put("press_out", "出站压力（MPa）");
            put("flow_inst_out", "瞬时流量（m³/h）");
            put("flow_totle_out", "流量计读数");
            put("water_val", "水量（m³）");
            put("temp_in", "入站温度(℃)");
            put("press_in", "入站压力（MPa）");
            put("flow_inst_in", "瞬时流量（m³/h）");
            put("flow_totle_in", "流量计读数");
            put("liquid_val", "液量（m³）");
        }};
    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        List<server.db.primary.model.sheet.SimpleSheet> reportData = getData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "简单报表（" + searchDateStr + "）";
        List<SimpleCell> extraHeaderCell = new ArrayList<SimpleCell>() {{
            add(new SimpleCell("", 3));
            add(new SimpleCell("掺水汇管（出站）", 5));
            add(new SimpleCell("集油汇管（进站）", 5));
        }};
        ExcelExport.export(WorkBookVersion.Excel97_2003, response, fileName, fileName, getColumnMap(), true, reportData, true, extraHeaderCell);
    }

    private List<server.db.primary.model.sheet.SimpleSheet> getData() {
        List<server.db.primary.model.sheet.SimpleSheet> list = new ArrayList<>();
        //        此处实际由数据库获取数据
        list.add(new server.db.primary.model.sheet.SimpleSheet().setReportDate("00:00").setPress_in(11D).setPress_out(224D));
        list.add(new server.db.primary.model.sheet.SimpleSheet().setReportDate("01:00"));
        list.add(new server.db.primary.model.sheet.SimpleSheet().setReportDate("02:00").setPress_in(51D));
        list.add(new server.db.primary.model.sheet.SimpleSheet().setReportDate("03:00").setPress_in(16D).setPress_out(224D));
        return list;
    }
}
