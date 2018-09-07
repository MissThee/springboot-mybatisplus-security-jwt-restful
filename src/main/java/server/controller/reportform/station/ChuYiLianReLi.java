package server.controller.reportform.station;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.reportform.ReportStationDataCylOil0_Res;
import server.db.primary.model.reportform.ReportStationDataCylOil_RL_Res;
import server.service.FunReportStationDataCylOil0Service;
import server.service.FunReportStationDataCylOil1Service;
import server.service.FunReportStationDataCylOilService;
import server.tool.ExcelUtils;
import server.tool.Res;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/reportForm/ChuYiLianReLi")
@RestController("FunChuYiLianReLi")
public class ChuYiLianReLi {
    @Autowired
    FunReportStationDataCylOilService funReportStationDataCylOilService;

    private Map<String, String> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("id", "");
            put("report_time", "时间");
            put("ti_208", "温度℃");
            put("pi_204", "压力MPa");
            put("ti_206", "出口温度℃");
            put("pi_202", "出口压力MPa");
            put("jrl_ck1", "进口温度℃");
            put("ti_202", "出口温度℃");
            put("jrl_ck2", "进口温度℃");
            put("ti_203", "出口温度℃");
            put("jrl_ck3", "进口温度℃");
            put("ti_204", "出口温度℃");
            put("ft_201", "流量计读数m³(瞬时)");
            put("ftq_201", "流量计读数m³(累计)");
            put("syj_ss2", "流量计读数m³(瞬时)");
            put("syj_lj2", "流量计读数m³");
            put("pi_201", "压力MPa");
            put("ti_201", "进口温度℃");//进口温度℃
            put("ft_202", "流量计读数(瞬时)m³/h");
            put("ftq_202", "流量计读数(累计)m³");
            put("pi_203", "管压MPa");
            put("ti_207", "温度℃");
            put("ryb_gy1", "管压MPa");
            put("ryb_wd1", "温度℃");
            put("ryb_gy2", "管压MPa");
            put("ryb_wd2", "温度℃");
            put("lng_ds", "流量计读数m³");
            put("lng_yl", "燃气/油量m³");
            put("zcq_ds", "流量计读数m³");
            put("zcq_yl", "燃气/油量m³");
        }};
    }

    @PostMapping()
    public Res dataTable(@RequestBody JSONObject bodyJO) throws ParseException {
        List<ReportStationDataCylOil_RL_Res> reportDataWaterWell = getData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", reportDataWaterWell);
        return Res.successData(jO);
    }

    @PatchMapping()
    public Res dataEdit(@RequestBody JSONObject bodyJO) {
        ReportStationDataCylOil_RL_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportStationDataCylOil_RL_Res.class);
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

        List<ReportStationDataCylOil_RL_Res> reportData = getData(bodyJO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "楚一联热力系统综合日报表（" + searchDateStr + "）";
        //以下表头格式使用ExcelReadTool类生成，代码打印在控制台，手动复制到本处
        List<ExcelUtils.HeaderCell> extraHeaderCell = new ArrayList<ExcelUtils.HeaderCell>() {{
//行分隔
            add(new ExcelUtils.HeaderCell("项目", 0, 0, 1, 2));
            add(new ExcelUtils.HeaderCell("燃油换热器", 1, 0, 2, 2));
            add(new ExcelUtils.HeaderCell("加热炉", 3, 0, 8, 1));
            add(new ExcelUtils.HeaderCell("水源井", 11, 0, 4, 1));
            add(new ExcelUtils.HeaderCell("热水泵(3台)", 15, 0, 4, 1));
            add(new ExcelUtils.HeaderCell("燃油泵", 19, 0, 6, 1));
            add(new ExcelUtils.HeaderCell("燃油(气)", 25, 0, 4, 1));
//行分隔
            add(new ExcelUtils.HeaderCell("汇管", 3, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("1#", 5, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("2#", 7, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("3#", 9, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("1#", 11, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("2#", 13, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("出口汇管", 15, 1, 4, 1));
            add(new ExcelUtils.HeaderCell("汇管", 19, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("1#", 21, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("2#", 23, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("LNG#", 25, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("自产气#", 27, 1, 2, 1));
//行分隔
            add(new ExcelUtils.HeaderCell("时间", 0, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 1, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 2, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口温度℃", 3, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口压力MPa", 4, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("进口温度℃", 5, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口温度℃", 6, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("进口温度℃", 7, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口温度℃", 8, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("进口温度℃", 9, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口温度℃", 10, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数m³(瞬时)", 11, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数m³(累计)", 12, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数m³(瞬时)", 13, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数m³", 14, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("压力MPa", 15, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 16, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数(瞬时)m³/h", 17, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数(累计)m³", 18, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("管压MPa", 19, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃ ", 20, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("管压MPa", 21, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 22, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("管压MPa", 23, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("温度℃", 24, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数m³", 25, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("燃气/油量m³", 26, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("流量计读数m³", 27, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("燃气/油量m³", 28, 2, 1, 1));
        }};
        ExcelUtils.LastRowColumnNum lastRowColumnNum = ExcelUtils.addRowsByData(wb, 0, 0, 0, fileName, getColumnMap(), false, reportData, false, extraHeaderCell);
        rowNum = lastRowColumnNum.getRowNum();
        {
            HSSFRow row = sheet.createRow(rowNum);
            int columnIndex = 0;
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("小结");
                HSSFCellStyle hssfCellStyle = ExcelUtils.headerStyle(wb);
                hssfCellStyle.setAlignment(HorizontalAlignment.LEFT);
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 28);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, hssfCellStyle);
            }
            rowNum++;
        }
        List<ReportStationDataCylOil0_Res> formData = getFormData(bodyJO);
        for (ReportStationDataCylOil0_Res res : formData) {
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
                cell.setCellValue("LNG量(m³)：");
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 2 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getVal_oil() != null) {
                    cell.setCellValue(res.getVal_oil());
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                columnIndex = columnIndex + 2 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellValue("自产气量(m³)：");
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 3);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 3 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getVal_gas() != null) {
                    cell.setCellValue(res.getVal_gas());
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 8);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                columnIndex = columnIndex + 8 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellValue("备注：");
                cell.setCellStyle(ExcelUtils.headerStyle(wb));
                columnIndex++;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getRemark() != null) {
                    cell.setCellValue(res.getRemark());
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 7);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
            }
            rowNum++;
        }

        ExcelUtils.responseOut(response, wb, fileName);
    }

    private List<ReportStationDataCylOil_RL_Res> getData(JSONObject bodyJO) throws ParseException {
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        List<ReportStationDataCylOil_RL_Res> res = funReportStationDataCylOilService.selectRLReportData(searchDateStr, getColumnMap());
        return res;
    }


    @Autowired
    FunReportStationDataCylOil0Service funReportStationDataCylOil0Service;

    private Map<String, String> getFormColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("id", "");
            put("report_hour", "时间");
            put("val_oil", "LNG量(m³)：");
            put("val_gas", "自产气量(m³)：");
            put("remark", "备注");
        }};
    }

    @PostMapping("/form")
    public Res formDataTable(@RequestBody JSONObject bodyJO) {
        List<ReportStationDataCylOil0_Res> res = getFormData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", res);
        return Res.successData(jO);
    }

    @PatchMapping("/form")
    public Res formDataEdit(@RequestBody JSONObject bodyJO) {
        ReportStationDataCylOil0_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportStationDataCylOil0_Res.class);
        if (reportData.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        if (funReportStationDataCylOil0Service.updateData(reportData)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    private List<ReportStationDataCylOil0_Res> getFormData(JSONObject bodyJO) {
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        return funReportStationDataCylOil0Service.selectReportForm(searchDateStr, getFormColumnMap());
    }
}
