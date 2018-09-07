package server.controller.reportform.station;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.reportform.ReportStationDataCylWater0_Res;
import server.db.primary.model.reportform.ReportStationDataCylWater_Res;
import server.service.FunReportStationDataCylWater0Service;
import server.service.FunReportStationDataCylWaterService;
import server.tool.ExcelUtils;
import server.tool.Res;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/reportForm/ChuYiLianZhuShui")
@RestController("FunChuYiLianZhuShui")
public class ChuYiLianZhuShui {
    @Autowired
    FunReportStationDataCylWaterService funReportStationDataCylWaterService;

    private ArrayList<ExcelUtils.DataColumn> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空
        return new ArrayList<ExcelUtils.DataColumn>() {{
            add(new ExcelUtils.DataColumn("id", ""));
            add(new ExcelUtils.DataColumn("report_time", "1"));
            add(new ExcelUtils.DataColumn("pi_419", "1"));
            add(new ExcelUtils.DataColumn("pi_418", "1"));
            add(new ExcelUtils.DataColumn("pi_417", "1"));
            add(new ExcelUtils.DataColumn("pi_416", "1"));
            add(new ExcelUtils.DataColumn("pi_415", "1"));
            add(new ExcelUtils.DataColumn("pi_414", "1"));
            add(new ExcelUtils.DataColumn("pi_413", "1"));
            add(new ExcelUtils.DataColumn("pi_412", "1"));
            add(new ExcelUtils.DataColumn("pi_411", "1"));
            add(new ExcelUtils.DataColumn("pi_420", "1"));
            add(new ExcelUtils.DataColumn("ft_403", "1"));
            add(new ExcelUtils.DataColumn("ft_888", "1"));
            add(new ExcelUtils.DataColumn("shuiliang1", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("ftq_404", "1"));
            add(new ExcelUtils.DataColumn("ft_404", "1"));
            add(new ExcelUtils.DataColumn("shuiliang2", "1").setNoDataBaseColumn());
            add(new ExcelUtils.DataColumn("pi_410", "1", 3));
            add(new ExcelUtils.DataColumn("li_406", "1"));
            add(new ExcelUtils.DataColumn("li_405", "1"));
            add(new ExcelUtils.DataColumn("li_407", "1"));
            add(new ExcelUtils.DataColumn("li_408", "1"));
            add(new ExcelUtils.DataColumn("ft_888_1", "1"));
            add(new ExcelUtils.DataColumn("yewei1", "1").setEmptyData());
            add(new ExcelUtils.DataColumn("yewei2", "1").setEmptyData());
            add(new ExcelUtils.DataColumn("li_401", "1"));
            add(new ExcelUtils.DataColumn("li_402", "1"));
            add(new ExcelUtils.DataColumn("pi_401", "1"));
            add(new ExcelUtils.DataColumn("pi_402", "1"));
            add(new ExcelUtils.DataColumn("pi_403", "1"));
            add(new ExcelUtils.DataColumn("li_403", "1"));
            add(new ExcelUtils.DataColumn("li_404", "1"));
            add(new ExcelUtils.DataColumn("pi_404", "1"));
            add(new ExcelUtils.DataColumn("pi_405", "1"));
            add(new ExcelUtils.DataColumn("pi_408", "1"));
            add(new ExcelUtils.DataColumn("ftq_401", "1"));
            add(new ExcelUtils.DataColumn("ft_401", "1"));
            add(new ExcelUtils.DataColumn("pi_406", "1"));
            add(new ExcelUtils.DataColumn("pi_407", "1"));
            add(new ExcelUtils.DataColumn("pi_409", "1"));
            add(new ExcelUtils.DataColumn("ftq_402", "1"));
            add(new ExcelUtils.DataColumn("ft_402", "1"));
            add(new ExcelUtils.DataColumn("jybh", "1"));
            add(new ExcelUtils.DataColumn("jyhs", "1"));
            add(new ExcelUtils.DataColumn("jyzg", "1"));
            add(new ExcelUtils.DataColumn("jyjs", "1"));
            add(new ExcelUtils.DataColumn("jyxn", "1"));
            add(new ExcelUtils.DataColumn("wsb_by1", "1"));
            add(new ExcelUtils.DataColumn("wsb_by2", "1"));
            add(new ExcelUtils.DataColumn("wsb_by3", "1"));
            add(new ExcelUtils.DataColumn("wshy", "1"));
            add(new ExcelUtils.DataColumn("qsb_by", "1"));
            add(new ExcelUtils.DataColumn("qsb_ss", "1"));
            add(new ExcelUtils.DataColumn("qsb_lj", "1"));
        }};
    }

    @PostMapping()
    public Res dataTable(@RequestBody JSONObject bodyJO) throws ParseException {
        List<ReportStationDataCylWater_Res> reportDataWaterWell = getData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", reportDataWaterWell);
        return Res.successData(jO);
    }

    @PatchMapping()
    public Res dataEdit(@RequestBody JSONObject bodyJO) {
        ReportStationDataCylWater_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportStationDataCylWater_Res.class);
        if (reportData.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        if (funReportStationDataCylWaterService.updateData(reportData)) {
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

        List<ReportStationDataCylWater_Res> reportData = getData(bodyJO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "楚一联注水综合日报表（" + searchDateStr + "）";
        //以下表头格式使用ExcelReadTool类生成，代码打印在控制台，手动复制到本处
        List<ExcelUtils.HeaderCell> extraHeaderCell = new ArrayList<ExcelUtils.HeaderCell>() {{
//行分隔
            add(new ExcelUtils.HeaderCell("时间", 0, 0, 1, 4));
            add(new ExcelUtils.HeaderCell("注水泵", 1, 0, 16, 1));
            add(new ExcelUtils.HeaderCell("喂水泵", 17, 0, 3, 1));
            add(new ExcelUtils.HeaderCell("储水罐液位", 20, 0, 4, 1));
            add(new ExcelUtils.HeaderCell("总注水量", 24, 0, 1, 3));
            add(new ExcelUtils.HeaderCell("生化池", 25, 0, 2, 1));
            add(new ExcelUtils.HeaderCell("污水池", 27, 0, 2, 1));
            add(new ExcelUtils.HeaderCell("生化提升泵", 29, 0, 3, 1));
            add(new ExcelUtils.HeaderCell("污水提升泵", 32, 0, 2, 1));
            add(new ExcelUtils.HeaderCell("多功能过滤器", 34, 0, 10, 1));
            add(new ExcelUtils.HeaderCell("日加药量(kg)", 44, 0, 5, 1));
            add(new ExcelUtils.HeaderCell("污水泵", 49, 0, 3, 1));
            add(new ExcelUtils.HeaderCell("污水含油（mg/l）", 52, 0, 1, 4));
            add(new ExcelUtils.HeaderCell("清水泵", 53, 0, 3, 1));
//行分隔
            add(new ExcelUtils.HeaderCell("1#", 1, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("2#", 2, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("3#", 3, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("4#", 4, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("5#", 5, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("6#", 6, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("7#", 7, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("8#", 8, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("9#", 9, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("干压", 10, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("出口汇管流量计读数(累计)", 11, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("出口汇管流量计读数(瞬时)", 12, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("水量", 13, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("回水汇管流量计读数(累计)", 14, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("回水汇管流量计读数(瞬时)", 15, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("水量", 16, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("1#", 17, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("2#", 18, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("3#", 19, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("调节罐", 20, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("沉降罐", 21, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("污水罐", 22, 1, 2, 1));
            add(new ExcelUtils.HeaderCell("1#", 25, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("2#", 26, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("生化池处", 27, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("干化池处", 28, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("1#", 29, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("2#", 30, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("3#", 31, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("1#", 32, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("2#", 33, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("1.0", 34, 1, 5, 1));
            add(new ExcelUtils.HeaderCell("2.0", 39, 1, 5, 1));
            add(new ExcelUtils.HeaderCell("运行加药泵号", 44, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("缓蚀剂", 45, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("阻垢剂", 46, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("净水剂", 47, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("絮凝剂", 48, 1, 1, 3));
            add(new ExcelUtils.HeaderCell("1#", 49, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("2#", 50, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("3#", 51, 1, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 53, 1, 1, 2));
            add(new ExcelUtils.HeaderCell("流量计", 54, 1, 2, 1));
//行分隔
            add(new ExcelUtils.HeaderCell("泵压", 1, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 2, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 3, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 4, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 5, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 6, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 7, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 8, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 9, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口汇管压力", 17, 2, 3, 1));
            add(new ExcelUtils.HeaderCell("1#", 22, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("2#", 23, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("液位", 25, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("液位", 26, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("液位", 27, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("液位", 28, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 29, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 30, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 31, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 32, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 33, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("进口压力", 34, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口压力", 35, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("一级过滤出口压力", 36, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口流量(累计)", 37, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口流量(瞬时)", 38, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("进口压力", 39, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口压力", 40, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("一级过滤出口压力", 41, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口流量(累计)", 42, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("出口流量(瞬时)", 43, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 49, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 50, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("泵压", 51, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("瞬时读数", 54, 2, 1, 1));
            add(new ExcelUtils.HeaderCell("累计读数", 55, 2, 1, 1));
//行分隔
            add(new ExcelUtils.HeaderCell("MPa", 1, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 2, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 3, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 4, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 5, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 6, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 7, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 8, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 9, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 10, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³", 11, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³", 12, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³", 13, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³", 14, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³", 15, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³", 16, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 17, 3, 3, 1));
            add(new ExcelUtils.HeaderCell("m", 20, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 21, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 22, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 23, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³", 24, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 25, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 26, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 27, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m", 28, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 29, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 30, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 31, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 32, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 33, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 34, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 35, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 36, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³", 37, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³/h", 38, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 39, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 40, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 41, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³", 42, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³/h", 43, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 49, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 50, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 51, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("MPa", 53, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³", 54, 3, 1, 1));
            add(new ExcelUtils.HeaderCell("m³", 55, 3, 1, 1));
        }};
        ExcelUtils.LastRowColumnNum lastRowColumnNum = ExcelUtils.addRowsByData(wb, 0, 0, 0, fileName, getColumnMap(), false, reportData, false, extraHeaderCell);
        rowNum = lastRowColumnNum.getRowNum();

        {
            HSSFRow row = sheet.createRow(rowNum);
            int columnIndex = 0;
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("");
                HSSFCellStyle hssfCellStyle = ExcelUtils.headerStyle(wb);
                hssfCellStyle.setAlignment(HorizontalAlignment.LEFT);
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 28);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, hssfCellStyle);
            }
            rowNum++;
        }
        {
            HSSFRow row = sheet.createRow(rowNum);
            int columnIndex = 0;
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("过滤器反冲洗记录");
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 3);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 3 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("收油记录");
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 3);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));

            }
            rowNum++;
        }
        {
            HSSFRow row = sheet.createRow(rowNum);
            int columnIndex = 0;
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("时间");
                cell.setCellStyle(ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("罐号");
                cell.setCellStyle(ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1;

            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("排量 m³/h");
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("时间");
                cell.setCellStyle(ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("除油罐");
                ExcelUtils.setColumnWidth(sheet, columnIndex, cell.getStringCellValue(), true, 1);
                cell.setCellStyle(ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("调节罐");
                ExcelUtils.setColumnWidth(sheet, columnIndex, cell.getStringCellValue(), true, 1);
                cell.setCellStyle(ExcelUtils.headerStyle(wb));
                columnIndex = columnIndex + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("污水池");
                ExcelUtils.setColumnWidth(sheet, columnIndex, cell.getStringCellValue(), true, 1);
                cell.setCellStyle(ExcelUtils.headerStyle(wb));
            }
            rowNum++;
        }

        List<ReportStationDataCylWater0_Res> formData = getFormData(bodyJO);
        ReportStationDataCylWater0_Res res = new ReportStationDataCylWater0_Res();
        if (formData != null && formData.size() > 0) {
            res = formData.get(0);
        }
        {
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
                cell.setCellType(CellType.STRING);
                if (res.getFcx_gh() != null) {
                    cell.setCellValue(res.getFcx_gh());
                }

                cell.setCellStyle(ExcelUtils.dataStyle(wb));
                columnIndex++;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getFcx_pl() != null) {
                    cell.setCellValue(res.getFcx_pl());
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
                columnIndex = columnIndex + 1 + 1;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getReport_hour1() != null) {
                    cell.setCellValue(res.getReport_hour1());
                    ExcelUtils.setColumnWidth(sheet, columnIndex, cell.getStringCellValue(), true, 1);
                }
                cell.setCellStyle(ExcelUtils.dataStyle(wb));
                columnIndex++;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getSy_cyg() != null) {
                    cell.setCellValue(res.getSy_cyg());
                }
                cell.setCellStyle(ExcelUtils.dataStyle(wb));
                columnIndex++;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getSy_tjg() != null) {
                    cell.setCellValue(res.getSy_tjg());
                }
                cell.setCellStyle(ExcelUtils.dataStyle(wb));
                columnIndex++;
            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getSy_wsc() != null) {
                    cell.setCellValue(res.getSy_wsc());
                }
                cell.setCellStyle(ExcelUtils.dataStyle(wb));
            }
            rowNum++;
        }
        {
            HSSFRow row = sheet.createRow(rowNum);
            int columnIndex = 0;
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("备注");
                cell.setCellStyle(ExcelUtils.headerStyle(wb));
                columnIndex++;

            }
            {
                HSSFCell cell = row.createCell(columnIndex);
                cell.setCellType(CellType.STRING);
                if (res.getBz() != null) {
                    cell.setCellValue(res.getBz());
                }
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 54);
                sheet.addMergedRegion(cellRangeAddress);
                ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
            }
            rowNum++;
        }
        {
            int columnIndex = 8;
            HSSFRow row = sheet.getRow(rowNum - 4);
            HSSFCell cell = row.createCell(columnIndex);
            cell.setCellType(CellType.STRING);
            cell.setCellValue("");
            CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum - 4, rowNum - 2, columnIndex, columnIndex + 47);
            sheet.addMergedRegion(cellRangeAddress);
            ExcelUtils.setRegionStyle(sheet, cellRangeAddress, ExcelUtils.dataStyle(wb));
        }
        ExcelUtils.responseOut(response, wb, fileName);
    }

    private List<ReportStationDataCylWater_Res> getData(JSONObject bodyJO) throws ParseException {
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        return funReportStationDataCylWaterService.selectReportData(searchDateStr, getColumnMap());
    }


    @Autowired
    FunReportStationDataCylWater0Service funReportStationDataCylWater0Service;

    private Map<String, String> getFormColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空值
        return new LinkedHashMap<String, String>() {{
            put("id", "");
            put("report_hour", "1");
            put("fcx_gh", "1");
            put("fcx_pl", "1");
            put("report_hour1", "1");
            put("sy_cyg", "1");
            put("sy_tjg", "1");
            put("sy_wsc", "1");
            put("bz", "1");
        }};
    }

    @PostMapping("/form")
    public Res formDataTable(@RequestBody JSONObject bodyJO) {
        List<ReportStationDataCylWater0_Res> res = getFormData(bodyJO);
        JSONObject jO = new JSONObject();
        jO.put("Production", res);
        return Res.successData(jO);
    }

    @PatchMapping("/form")
    public Res formDataEdit(@RequestBody JSONObject bodyJO) {
        ReportStationDataCylWater0_Res reportData = bodyJO.getJSONObject("editData").toJavaObject(ReportStationDataCylWater0_Res.class);
        if (reportData.getId() == null) {
            return Res.failureMsg("修改失败，无id");
        }
        if (funReportStationDataCylWater0Service.updateData(reportData)) {
            return Res.successMsg("修改成功");
        } else {
            return Res.failureMsg("修改失败");
        }
    }

    private List<ReportStationDataCylWater0_Res> getFormData(JSONObject bodyJO) {
        Date searchDate = bodyJO.getDate("searchDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        return funReportStationDataCylWater0Service.selectReportForm(searchDateStr, getFormColumnMap());
    }
}
