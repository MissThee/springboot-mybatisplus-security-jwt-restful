package server.controller.reportform.raoyang;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.reportform.ReportRaoYangChanShui_Res;
import server.service.FunReportRaoYangChanShuiService;
import server.tool.ExcelUtils;
import server.tool.Res;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/reportForm/RaoYangChanShui")
@RestController("FunRaoYangChanShui")
public class RaoYangChanShui {
    @Autowired
    FunReportRaoYangChanShuiService funRaoYangChanShuiService;

    private ArrayList<ExcelUtils.DataColumn> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空
        return new ArrayList<ExcelUtils.DataColumn>() {{
            add(new ExcelUtils.DataColumn("id", "").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("name", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("open_wells", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("liquid_day", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("oil_day", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("containing", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("temp_water", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("press_water", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("water_val", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("temp_oil", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("liquid_val", "1").setNoDataBaseColumn());
        }};
    }

    @PostMapping()
    public Res dataTable(@RequestBody JSONObject bodyJO) throws ParseException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<ReportRaoYangChanShui_Res> reportDataWaterWell = getData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", reportDataWaterWell);
        return Res.successData(jO);
    }

//    @PatchMapping()
//    public Res dataEdit(@RequestBody JSONObject bodyJO) {
//        ReportRaoYangZongHe reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportRaoYangZongHe.class);
//        if (reportData.getId() == null) {
//            return Res.failureMsg("修改失败，无id");
//        }
//        if (funRaoYangChanShuiService.updateData(reportData)) {
//            return Res.successMsg("修改成功");
//        } else {
//            return Res.failureMsg("修改失败");
//        }
//    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        wb.createSheet();

        List<ReportRaoYangChanShui_Res> reportData = getData(bodyJO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "饶阳工区掺水阀组日报表（" + searchDateStr + "）";
        //以下表头格式使用ExcelReadTool类生成，代码打印在控制台，手动复制到本处
        List<ExcelUtils.HeaderCell> extraHeaderCell = new ArrayList<ExcelUtils.HeaderCell>() {{
            add(new ExcelUtils.HeaderCell("序号", 0, 0, 1, 2));
            add(new ExcelUtils.HeaderCell("阀组名称", 1, 0, 1, 2));
            add(new ExcelUtils.HeaderCell("开井数", 2, 0, 1, 2));
            add(new ExcelUtils.HeaderCell("日产液", 3, 0, 1, 2));
            add(new ExcelUtils.HeaderCell("日产油", 4, 0, 1, 2));
            add(new ExcelUtils.HeaderCell("含水", 5, 0, 1, 2));
            add(new ExcelUtils.HeaderCell("掺水汇管（出站）", 6, 0, 3, 1));
            add(new ExcelUtils.HeaderCell("集油汇管", 9, 0, 2, 1));
//行分隔
            add(new ExcelUtils.HeaderCell("温度", 6, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("压力", 7, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("水量", 8, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("温度", 9, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("液量", 10, 1, 1, 1));
        }};
        ExcelUtils.addRowsByData(wb, 0, 0,0, fileName, getColumnMap(), false, reportData, true,   extraHeaderCell);
        ExcelUtils.responseOut(response, wb, fileName);
    }

    private List<ReportRaoYangChanShui_Res> getData(JSONObject bodyJO) throws ParseException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        return funRaoYangChanShuiService.selectReportData(searchDateStr, getColumnMap());
    }
}
