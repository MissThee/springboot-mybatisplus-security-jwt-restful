package server.controller.reportform;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.reportform.ReportDataOwaterLoop_Month1_Res;
import server.db.primary.model.reportform.ReportDataOwaterLoop_Month2_Res;
import server.security.JavaJWT;
import server.service.FunReportConfStationService;
import server.service.FunReportDataOwaterLoopService;
import server.tool.ExcelUtils;
import server.tool.Res;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@RequestMapping("/reportForm/")
@RestController("FunOwaterReportMonthController")
public class OwaterReportMonthController {

    @Autowired
    FunReportConfStationService funReportConfStationService;

    @PostMapping("/owaterMonth/treeMenu")
    public Res treeMenu(@RequestHeader(value = "Authorization",required = false) String token) {
        List<Long> areaIds = token == null ? null : JavaJWT.getAreaIds(token);
        JSONObject jO = new JSONObject();
        jO.put("treeMenu", funReportConfStationService.selectTreeByAreaIdsAndIdRange(areaIds, 2L));
        return Res.successData(jO);
    }

    @Autowired
    FunReportDataOwaterLoopService funReportDataOwaterLoopService;

    private Map<String, String> getMonth1ColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("date", "日期"); 
            put("b_1", "掺水");//楚一联南环
            put("b_2", "集油");//
            put("c_1", "掺水");//楚一联东环
            put("c_2", "集油");//
            put("d_1", "掺水");//楚一联西环
            put("d_2", "集油");//
            put("e_1", "掺水");//28-1阀组西环
            put("e_2", "集油");//
            put("f_1", "掺水");//28-1阀组东环
            put("f_2", "集油");//
            put("g_1", "掺水");//28-1阀组南环
            put("g_2", "集油");//
            put("h_1", "掺水");//28-1阀组北环
            put("h_2", "集油");//
            put("i_1", "掺水");//102-1阀组西一环
            put("i_2", "集油");//
            put("j_1", "掺水");//102-1阀组西二环
            put("j_2", "集油");//
            put("k_1", "掺水");//102-1阀组南环
            put("k_2", "集油");//
            put("m_1", "掺水");//102-1阀组东环
            put("m_2", "集油");//
        }};
    }

    @PostMapping("/owaterMonth1")
    public Res month1DataTable(@RequestBody JSONObject bodyJO) throws Exception {
        List<ReportDataOwaterLoop_Month1_Res> reportData = getMonth1Data(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", reportData);
        return Res.successData(jO);
    }

    @PostMapping("/owaterMonth1/excel")
    public void month1Excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        List<ReportDataOwaterLoop_Month1_Res> reportDataOilWell = getMonth1Data(bodyJO);
        String searchDate = checkDate(bodyJO.getString("searchDate"));
        String fileName = "掺水阀组月报数据（" + searchDate + "）";
        List<ExcelUtils.HeaderCell> extraHeaderCell = new ArrayList<ExcelUtils.HeaderCell>() {{
            add(new ExcelUtils.HeaderCell("", 2));
            add(new ExcelUtils.HeaderCell("楚一联南环", 2));
            add(new ExcelUtils.HeaderCell("楚一联东环", 2));
            add(new ExcelUtils.HeaderCell("楚一联西环", 2));
            add(new ExcelUtils.HeaderCell("28-1阀组西环", 2));
            add(new ExcelUtils.HeaderCell("28-1阀组东环", 2));
            add(new ExcelUtils.HeaderCell("28-1阀组南环", 2));
            add(new ExcelUtils.HeaderCell("28-1阀组北环", 2));
            add(new ExcelUtils.HeaderCell("102-1阀组西一环", 2));
            add(new ExcelUtils.HeaderCell("102-1阀组西二环", 2));
            add(new ExcelUtils.HeaderCell("102-1阀组南环", 2));
            add(new ExcelUtils.HeaderCell("102-1阀组东环", 2));
        }};
        ExcelUtils.export(response, fileName, fileName, getMonth1ColumnMap(), true, reportDataOilWell, true, extraHeaderCell);
    }

    private List<ReportDataOwaterLoop_Month1_Res> getMonth1Data(JSONObject bodyJO) throws Exception {
        Long stationId = 1L;// bodyJO.getLong("stationId");
        String searchDate = checkDate(bodyJO.getString("searchDate"));
        return funReportDataOwaterLoopService.selectReportDataMonth1(stationId, searchDate, getMonth1ColumnMap());
    }

    private Map<String, String> getMonth2ColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("date", "日期");
            put("b_1", "掺水");//楚二站东
            put("b_2", "集油");//
            put("c_1", "掺水");//楚二站西环北线
            put("c_2", "集油");//
            put("d_1", "掺水");//楚二站西环南线
            put("d_2", "集油");//
            put("e_1", "掺水");//楚二站南环北线
            put("e_2", "集油");//
            put("f_1", "掺水");//楚二站南环西线
            put("f_2", "集油");//
            put("g_1", "掺水");//楚二站南环东线
            put("g_2", "集油");//
            put("h_1", "掺水");//楚29-2阀组西环
            put("h_2", "集油");//
            put("i_1", "掺水");//楚29-2阀组北环
            put("i_2", "集油");//
            put("j_1", "掺水");//楚29-2阀组东环
            put("j_2", "集油");//
            put("k_1", "掺水");//楚29-2阀组南环
            put("k_2", "集油");//

        }};
    }

    @PostMapping("/owaterMonth2")
    public Res month2DataTable(@RequestBody JSONObject bodyJO) throws Exception {
        List<ReportDataOwaterLoop_Month2_Res> reportDataOilWell = getMonth2Data(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", reportDataOilWell);
        return Res.successData(jO);
    }

    @PostMapping("/owaterMonth2/excel")
    public void month2Excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        List<ReportDataOwaterLoop_Month2_Res> reportData = getMonth2Data(bodyJO);
        String searchDate = checkDate(bodyJO.getString("searchDate"));
        String fileName = "掺水阀组月报数据（" + searchDate + "）";
        List<ExcelUtils.HeaderCell> extraHeaderCell = new ArrayList<ExcelUtils.HeaderCell>() {{
            add(new ExcelUtils.HeaderCell("", 2));
            add(new ExcelUtils.HeaderCell("楚二站东", 2));
            add(new ExcelUtils.HeaderCell("楚二站西环北线", 2));
            add(new ExcelUtils.HeaderCell("楚二站西环南线", 2));
            add(new ExcelUtils.HeaderCell("楚二站南环北线", 2));
            add(new ExcelUtils.HeaderCell("楚二站南环西线", 2));
            add(new ExcelUtils.HeaderCell("楚二站南环东线", 2));
            add(new ExcelUtils.HeaderCell("楚29-2阀组西环", 2));
            add(new ExcelUtils.HeaderCell("楚29-2阀组北环", 2));
            add(new ExcelUtils.HeaderCell("楚29-2阀组东环", 2));
            add(new ExcelUtils.HeaderCell("楚29-2阀组南环", 2));
        }};
        ExcelUtils.export(response, fileName, fileName, getMonth2ColumnMap(), true, reportData, true, extraHeaderCell);
    }

    private List<ReportDataOwaterLoop_Month2_Res> getMonth2Data(JSONObject bodyJO) throws Exception {
        Long stationId = 2L;// bodyJO.getLong("stationId");
        String searchDate = checkDate(bodyJO.getString("searchDate"));
        return funReportDataOwaterLoopService.selectReportDataMonth2(stationId, searchDate, getMonth2ColumnMap());
    }

    private String checkDate(String searchDate) throws Exception {
        if (searchDate == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            searchDate = sdf.format(new Date());
        } else {
            String pattern = "^\\d{4}-\\d{2}$";
            if (!Pattern.matches(pattern, searchDate)) {
                throw new Exception("时间格式有误: "+searchDate+" ，月报表需接收 yyyy-MM 格式参数");
            }
        }
        return searchDate;
    }
}


