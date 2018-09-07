package server.controller.reportform.station;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.reportform.ReportStationDataCylOil1_Res;
import server.db.primary.model.reportform.ReportStationDataCylOil_JS_Res;
import server.service.FunReportStationDataCylOil1Service;
import server.service.FunReportStationDataCylOilService;
import server.tool.ExcelUtils;
import server.tool.Res;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/reportForm/ChuYiLianJiShu")
@RestController("FunChuYiLianJiShu")
public class ChuYiLianJiShu {
    @Autowired
    FunReportStationDataCylOilService funReportStationDataCylOilService;

    private Map<String, String> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("id", "");
            put("report_time", "时间");
            put("li_321", "1液位");
            put("cygjm1", "界面");
            put("li_322", "2液位");
            put("cygjm2", "界面");
            put("li_323", "3液位");
            put("cygjm3", "界面");
            put("li_326", "4液位");
            put("li_327", "界面");
            put("li_324", "5液位");
            put("cygjm5", "界面");
            put("li_325", "6液位");
            put("cygjm6", "界面");
            put("pi_301", "1压力MPa");
            put("flqwd1", "1温度℃");
            put("li_302", "1油水液面m");
            put("li_301", "1油水界面m");
            put("pi_302", "2压力MPa");
            put("flqwd2", "2温度℃");
            put("li_315", "2油水液面m");
            put("li_314", "2油水界面m");
            put("pi_303", "汇管压力MPa");
            put("pi_305", "压力MPa");
            put("ti_301", "温度℃");
            put("ti_304", "油-温度℃");
            put("pi_306", "油-压力MPa");
            put("ti_305", "热水-温度℃");
            put("ti_302", "油出口-温度℃");
            put("ti_303", "油出口-温度℃");
            put("pi_307", "外输压力MPa");
            put("ti_306", "外输温度℃");
            put("wsds1", "1读数m³");
            put("wsyl1", "1液量m");
            put("wsds2", "2读数m³");
            put("wsyl2", "2液量m³");
            put("ti_101", "温度℃");
            put("pi_101", "压力MPa");
            put("ft_101", "流量计读数(瞬时)m³");
            put("ftq_101", "流量计读数(累计)m³");
            put("ti_106", "温度℃");
            put("pi_102", "压力MPa");
            put("ti_103", "温度℃");
            put("ti_102", "温度℃");
            put("jyxh", "加药型号");
            put("jyl", "加药量");
        }};
    }

    @PostMapping()
    public Res dataTable(@RequestBody JSONObject bodyJO) throws ParseException {
        List<ReportStationDataCylOil_JS_Res> reportDataWaterWell = getData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", reportDataWaterWell);
        return Res.successData(jO);
    }

    @PatchMapping()
    public Res dataEdit(@RequestBody JSONObject bodyJO) {
        ReportStationDataCylOil_JS_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportStationDataCylOil_JS_Res.class);
        if (reportData.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        if (funReportStationDataCylOilService.updateData(reportData)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        int rowNum = 0;

        List<ReportStationDataCylOil_JS_Res> reportData = getData(bodyJO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "楚一联集输综合日报表（" + searchDateStr + "）";
        //以下表头格式使用ExcelReadTool类生成，代码打印在控制台，手动复制到本处
        List<ExcelUtils.HeaderCell> extraHeaderCell = new ArrayList<ExcelUtils.HeaderCell>() {{
//行分隔
            add(new ExcelUtils.HeaderCell("项目", 0, 0, 1, 2));
            add(new ExcelUtils.HeaderCell("储油罐", 1, 0, 12, 1));
            add(new ExcelUtils.HeaderCell("三相分离器", 13, 0, 9, 1));
            add(new ExcelUtils.HeaderCell("脱水泵", 22, 0, 2, 1));
            add(new ExcelUtils.HeaderCell("脱水换热器", 24, 0, 5, 1));
            add(new ExcelUtils.HeaderCell("外输泵", 29, 0, 2, 1));
            add(new ExcelUtils.HeaderCell("外输流量计", 31, 0, 4, 1));
            add(new ExcelUtils.HeaderCell("掺水泵", 35, 0, 4, 1));
            add(new ExcelUtils.HeaderCell("掺水换热器", 39, 0, 4, 1));
            add(new ExcelUtils.HeaderCell("加药", 43, 0, 2, 1));
//行分隔
            add(new ExcelUtils.HeaderCell("1#", 1, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("2#", 3, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("3#", 5, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("4#", 7, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("5#", 9, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("6#", 11, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("1#", 13, 1, 4, 1));
            add(new ExcelUtils.HeaderCell("2#", 17, 1, 4, 1));
            add(new ExcelUtils.HeaderCell("天然气", 21, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("出口汇管", 22, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("出口汇管", 24, 1, 3, 1));
            add(new ExcelUtils.HeaderCell("1#", 27, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("2#", 28, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("外输压力MPa", 29, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("外输温度℃", 30, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("1#", 31, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("2#", 33, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("出口汇管", 35, 1, 4, 1));
            add(new ExcelUtils.HeaderCell("出口汇管", 39, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("1#", 41, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("2#", 42, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("加药型号", 43, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("加药量", 44, 1, 1, 3));
//行分隔
            add(new ExcelUtils.HeaderCell("时间", 0, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("液位", 1, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("界面", 2, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("液位", 3, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("界面", 4, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("液位", 5, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("界面", 6, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("液位", 7, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("界面", 8, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("液位", 9, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("界面", 10, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("液位", 11, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("界面", 12, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 13, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("温度℃", 14, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("油水液面m", 15, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("油水界面m", 16, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("压力MPa", 17, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("温度℃", 18, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("油水液面m", 19, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("油水界面m", 20, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("汇管压力MPa", 21, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("压力MPa", 22, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("温度℃", 23, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("油", 24, 2, 2, 1));
            add(new ExcelUtils.HeaderCell("热水", 26, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("油出口", 27, 2, 2, 1));
            add(new ExcelUtils.HeaderCell("读数m³", 31, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("液量m³", 32, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("读数m³", 33, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("液量m³", 34, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("温度℃", 35, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("压力MPa", 36, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("流量计读数(瞬时)m³", 37, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("流量计读数(累计)m³", 38, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("温度℃", 39, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("压力MPa", 40, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("温度℃", 41, 2, 1, 2));
            add(new ExcelUtils.HeaderCell("温度℃", 42, 2, 1, 2));
//行分隔
            add(new ExcelUtils.HeaderCell("m", 1, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 2, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 3, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 4, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 5, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("1m", 6, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 7, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 8, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 9, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 10, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 11, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 12, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 24, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 25, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 26, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 27, 3, 2, 1));

        }};
        ExcelUtils.LastRowColumnNum lastRowColumnNum = ExcelUtils.addRowsByData(wb, 0, 0, 0, fileName, getColumnMap(), false, reportData, false, extraHeaderCell);
        rowNum = lastRowColumnNum.getRowNum();

        List<ReportStationDataCylOil1_Res> formData = getFormData(bodyJO);
        StringBuilder allNote = new StringBuilder();
        for (ReportStationDataCylOil1_Res res : formData) {
            if (!StringUtils.isEmpty(res.getRemark())) {
                allNote.append(res.getRemark()).append("  ");
            }
            HSSFRow row = sheet.createRow(rowNum);
            int columnIndex = 0;
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getReport_hour() != null) {
                    cell.setCellValue(res.getReport_hour());
                }
                cell.setCellStyle(ExcelUtils.dataStyle(wb));
                columnIndex++;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellValue("进油量(m³):");
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getVal_fwater() != null) {
                    cell.setCellValue(res.getVal_fwater());
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellValue("外输量(吨):");
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getVal_inoil() != null) {
                    cell.setCellValue(res.getVal_inoil());
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellValue("楚二站外输量(吨):");
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getVal_wsoil() != null) {
                    cell.setCellValue(res.getVal_wsoil());
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellValue("处理量(吨):");
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getVal_moil() != null) {
                    cell.setCellValue(res.getVal_moil());
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellValue("库存(吨):");
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getVal_coil() != null) {
                    cell.setCellValue(res.getVal_coil());
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }

            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellValue("备注:");
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getRemark() != null) {
                    cell.setCellValue(res.getRemark());
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 21);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
            }
            rowNum++;
        }
        HSSFRow remarkRow = sheet.createRow(rowNum);
        {
            HSSFCell cell = remarkRow.createCell(0);
            cell.setCellValue("备注:");
            cell.setCellStyle(ExcelUtils.headerStyle(wb));
        }
        {
            HSSFCell cell = remarkRow.createCell(1);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(allNote.toString());
            CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, 1, 44);
            sheet.addMergedRegion(cellRangeAddress);
            ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
        }
        ExcelUtils.responseOut(response, wb, fileName);
//        ExcelUtils.export(response, fileName, title, getColumnMap(), false, reportData, false);
    }

    private List<ReportStationDataCylOil_JS_Res> getData(JSONObject bodyJO) throws ParseException {
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        return funReportStationDataCylOilService.selectJSReportData(searchDateStr, getColumnMap());
    }


    @Autowired
    FunReportStationDataCylOil1Service funReportStationDataCylOil1Service;

    private Map<String, String> getFormColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("id", "");
            put("report_hour", "时间");
            put("val_fwater", "进油量(m³)");
            put("val_inoil", "外输量(吨)");
            put("val_wsoil", "楚二站外输量(吨)");
            put("val_moil", "处理量(吨)");
            put("val_coil", "库存(吨)");
            put("remark", "备注");
        }};
    }

    @PostMapping("/form")
    public Res formDataTable(@RequestBody JSONObject bodyJO) {
        List<ReportStationDataCylOil1_Res> res = getFormData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", res);
        return Res.successData(jO);
    }

    @PatchMapping("/form")
    public Res formDataEdit(@RequestBody JSONObject bodyJO) {
        ReportStationDataCylOil1_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportStationDataCylOil1_Res.class);
        if (reportData.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        if (funReportStationDataCylOil1Service.updateData(reportData)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    private List<ReportStationDataCylOil1_Res> getFormData(JSONObject bodyJO) {
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        return funReportStationDataCylOil1Service.selectReportForm(searchDateStr, getFormColumnMap());
    }
}
