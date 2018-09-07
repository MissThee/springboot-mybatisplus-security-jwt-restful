package server.controller.reportform.raoyang;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.reportform.ReportRaoYangZongHe_Res;
import server.service.FunReportRaoYangZongHeService;
import server.tool.ExcelUtils;
import server.tool.Res;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/reportForm/RaoYangZongHe")
@RestController("FunRaoYangZongHe")
public class RaoYangZongHe {
    @Autowired
    FunReportRaoYangZongHeService funRaoYangZongHeService;

    private ArrayList<ExcelUtils.DataColumn> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空
        return new ArrayList<ExcelUtils.DataColumn>() {{
            add(new ExcelUtils.DataColumn("id", "").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("name", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("total_wells", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("open_wells", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("stop_wells", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("job_wells", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("abnormal_wells", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("liquid_daily", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("oil_daily", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("containing", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("ab_communication", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("stoppage_machine", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("over_alarm", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("working_alarm", "1").setNoDataBaseColumn());
        }};
    }

    @PostMapping()
    public Res dataTable(@RequestBody JSONObject bodyJO) throws ParseException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<ReportRaoYangZongHe_Res> reportDataWaterWell = getData(bodyJO);
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
//        if (funRaoYangZongHeService.updateData(reportData)) {
//            return Res.successMsg("修改成功");
//        } else {
//            return Res.failureMsg("修改失败");
//        }
//    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        wb.createSheet();
        List<ReportRaoYangZongHe_Res> reportData = getData(bodyJO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "饶阳工区综合日报表（" + searchDateStr + "）";
        //以下表头格式使用ExcelReadTool类生成，代码打印在控制台，手动复制到本处
        List<ExcelUtils.HeaderCell> extraHeaderCell = new ArrayList<ExcelUtils.HeaderCell>() {{
//行分隔
            add(new ExcelUtils.HeaderCell("名称",0,0,1,3));
            add(new ExcelUtils.HeaderCell("生产概况",1,0,8,1));
            add(new ExcelUtils.HeaderCell("数字化系统概况",9,0,2,1));
            add(new ExcelUtils.HeaderCell("报警系统概况",11,0,2,1));
//行分隔
            add(new ExcelUtils.HeaderCell("总井数",1,1,1,2));
            add(new ExcelUtils.HeaderCell("开井数",2,1,1,2));
            add(new ExcelUtils.HeaderCell("关井数",3,1,3,1));
            add(new ExcelUtils.HeaderCell("日产液",6,1,1,2));
            add(new ExcelUtils.HeaderCell("日产油",7,1,1,2));
            add(new ExcelUtils.HeaderCell("含水",8,1,1,2));
            add(new ExcelUtils.HeaderCell("单井通讯异常数",9,1,1,2));
            add(new ExcelUtils.HeaderCell("前端仪表故障数量",10,1,1,2));
            add(new ExcelUtils.HeaderCell("超限报警数",11,1,1,2));
            add(new ExcelUtils.HeaderCell("工况报警数",12,1,1,2));
//行分隔
            add(new ExcelUtils.HeaderCell("长停井",3,2,1,1));
            add(new ExcelUtils.HeaderCell("作业井",4,2,1,1));
            add(new ExcelUtils.HeaderCell("非正常停井",5,2,1,1));
        }};
        ExcelUtils.addRowsByData(wb, 0, 0, 0,fileName, getColumnMap(), false, reportData, false, extraHeaderCell);
        ExcelUtils.responseOut(response, wb, fileName);
    }

    private List<ReportRaoYangZongHe_Res> getData(JSONObject bodyJO) throws ParseException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        return funRaoYangZongHeService.selectReportData(searchDateStr, getColumnMap());
    }
}
