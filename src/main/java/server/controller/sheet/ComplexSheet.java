package server.controller.sheet;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.db.primary.model.sheet.ComplexSheetData;
import server.db.primary.model.sheet.ComplexSheetForm;
import server.tool.ExcelExport;
import server.tool.ListCompute;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/sheet/complex")
@RestController()
public class ComplexSheet {
    private final ExcelExport excelExport;
    private final ListCompute listCompute;

    @Autowired
    public ComplexSheet(ExcelExport excelExport, ListCompute listCompute) {
        this.excelExport = excelExport;
        this.listCompute = listCompute;
    }

    private ArrayList<ExcelExport.DataColumn> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空
        return new ArrayList<ExcelExport.DataColumn>() {{
            add(excelExport.new DataColumn("id", "").setNoDataBaseColumn());
            add(excelExport.new DataColumn("report_time", "1", 1));
            add(excelExport.new DataColumn("cyg_yw1", "1"));
            add(excelExport.new DataColumn("cyg_jm1", "1"));
            add(excelExport.new DataColumn("cyg_kr1", "1"));
            add(excelExport.new DataColumn("cyg_yw2", "1"));
            add(excelExport.new DataColumn("cyg_jm2", "1"));
            add(excelExport.new DataColumn("cyg_kr2", "1"));
            add(excelExport.new DataColumn("pi_1150", "1"));
            add(excelExport.new DataColumn("ti_1270", "1"));
            add(excelExport.new DataColumn("ft109", "1"));
            add(excelExport.new DataColumn("ti_1250", "1"));
            add(excelExport.new DataColumn("pi_1090", "1"));
            add(excelExport.new DataColumn("ti_1260", "1"));
            add(excelExport.new DataColumn("pi_1100", "1"));
            add(excelExport.new DataColumn("ti_1230", "1"));
            add(excelExport.new DataColumn("ti_1240", "1"));
            add(excelExport.new DataColumn("pi_1140", "1"));
            add(excelExport.new DataColumn("pi_1110", "1"));
            add(excelExport.new DataColumn("pi_1120", "1"));
            add(excelExport.new DataColumn("ti_1200", "1"));
            add(excelExport.new DataColumn("pi_1070", "1"));
            add(excelExport.new DataColumn("jiarelu1", "1").setNoDataBaseColumn());
            add(excelExport.new DataColumn("jiarelu2", "1").setNoDataBaseColumn());
            add(excelExport.new DataColumn("jiarelu3", "1").setNoDataBaseColumn());
            add(excelExport.new DataColumn("rq_ckyl", "1"));
            add(excelExport.new DataColumn("rq_ljds", "1"));
            add(excelExport.new DataColumn("ranqiliang", "1").setNoDataBaseColumn());
            add(excelExport.new DataColumn("pi_1060", "1"));
            add(excelExport.new DataColumn("ti_1150", "1"));
            add(excelExport.new DataColumn("ft104s", "1"));
            add(excelExport.new DataColumn("ft_1040", "1"));
            add(excelExport.new DataColumn("rsb_by1", "1"));
            add(excelExport.new DataColumn("rsb_by2", "1"));
            add(excelExport.new DataColumn("rsb_by3", "1"));
            add(excelExport.new DataColumn("lt_1030", "1"));
            add(excelExport.new DataColumn("hsg_wd", "1"));
            add(excelExport.new DataColumn("pi_1080", "1"));
            add(excelExport.new DataColumn("ti_1210", "1"));
            add(excelExport.new DataColumn("pi_1010", "1"));
            add(excelExport.new DataColumn("ti_1010", "1"));
            add(excelExport.new DataColumn("ft_1010", "1"));
            add(excelExport.new DataColumn("ft101s", "1"));
            add(excelExport.new DataColumn("cshrq_hgwd", "1"));
            add(excelExport.new DataColumn("ti_1020", "1"));
            add(excelExport.new DataColumn("ti_1030", "1"));
            add(excelExport.new DataColumn("ti_1220", "1"));
            add(excelExport.new DataColumn("ws_yl", "1"));
            add(excelExport.new DataColumn("ws_wd", "1"));
            add(excelExport.new DataColumn("ws_llj", "1"));
            add(excelExport.new DataColumn("yeliang", "1").setNoDataBaseColumn());
        }};
    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        int rowNum = 0;
        List<ComplexSheetData> reportData = getData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "复杂报表（" + searchDateStr + "）";
        //以下表头格式extraHeaderCell，使用ExcelReadTool类生成，代码打印在控制台，手动复制到本处
        List<ExcelExport.HeaderCell> extraHeaderCell = new ArrayList<ExcelExport.HeaderCell>() {{
//行分隔
            add(excelExport.new HeaderCell("项目", 0, 0, 1, 2));
            add(excelExport.new HeaderCell("储油罐", 1, 0, 6, 1));
            add(excelExport.new HeaderCell("外输泵", 7, 0, 3, 1));
            add(excelExport.new HeaderCell("进站（来液升温）换热器", 10, 0, 6, 1));
            add(excelExport.new HeaderCell("分离缓冲罐", 16, 0, 3, 1));
            add(excelExport.new HeaderCell("加热炉", 19, 0, 5, 1));
            add(excelExport.new HeaderCell("燃气", 24, 0, 3, 1));
            add(excelExport.new HeaderCell("热水泵", 27, 0, 7, 1));
            add(excelExport.new HeaderCell("热回水罐", 34, 0, 2, 1));
            add(excelExport.new HeaderCell("燃油泵", 36, 0, 2, 1));
            add(excelExport.new HeaderCell("掺水泵", 38, 0, 4, 1));
            add(excelExport.new HeaderCell("掺水换热器", 42, 0, 3, 1));
            add(excelExport.new HeaderCell("燃油换热器", 45, 0, 1, 1));
            add(excelExport.new HeaderCell("外输记录", 46, 0, 4, 1));
//行分隔
            add(excelExport.new HeaderCell("1#", 1, 1, 3, 1));
            add(excelExport.new HeaderCell("2#", 4, 1, 3, 1));
            add(excelExport.new HeaderCell("出口汇管", 7, 1, 3, 1));
            add(excelExport.new HeaderCell("出口汇管", 10, 1, 4, 1));
            add(excelExport.new HeaderCell("1#", 14, 1, 1, 1));
            add(excelExport.new HeaderCell("2#", 15, 1, 1, 1));
            add(excelExport.new HeaderCell("汇管", 16, 1, 1, 1));
            add(excelExport.new HeaderCell("1#", 17, 1, 1, 1));
            add(excelExport.new HeaderCell("2#", 18, 1, 1, 1));
            add(excelExport.new HeaderCell("出口汇管", 19, 1, 2, 2));
            add(excelExport.new HeaderCell("1#", 21, 1, 1, 2));
            add(excelExport.new HeaderCell("2#", 22, 1, 1, 2));
            add(excelExport.new HeaderCell("3#", 23, 1, 1, 2));
            add(excelExport.new HeaderCell("#", 24, 1, 3, 2));
            add(excelExport.new HeaderCell("出口汇管", 27, 1, 4, 2));
            add(excelExport.new HeaderCell("1#", 31, 1, 1, 2));
            add(excelExport.new HeaderCell("2#", 32, 1, 1, 2));
            add(excelExport.new HeaderCell("3#", 33, 1, 1, 2));
            add(excelExport.new HeaderCell("液位m", 34, 1, 1, 3));
            add(excelExport.new HeaderCell("温度℃", 35, 1, 1, 3));
            add(excelExport.new HeaderCell("#", 36, 1, 2, 1));
            add(excelExport.new HeaderCell("出口汇管", 38, 1, 4, 2));
            add(excelExport.new HeaderCell("出口汇管", 42, 1, 1, 2));
            add(excelExport.new HeaderCell("1#", 43, 1, 1, 2));
            add(excelExport.new HeaderCell("2#", 44, 1, 1, 2));
            add(excelExport.new HeaderCell("油", 45, 1, 1, 2));
            add(excelExport.new HeaderCell("压力MPa", 46, 1, 1, 3));
            add(excelExport.new HeaderCell("温度℃", 47, 1, 1, 3));
            add(excelExport.new HeaderCell("流量计读数m³", 48, 1, 1, 3));
            add(excelExport.new HeaderCell("液量m³", 49, 1, 1, 3));
//行分隔
            add(excelExport.new HeaderCell("时间", 0, 2, 1, 2));
            add(excelExport.new HeaderCell("液位", 1, 2, 1, 1));
            add(excelExport.new HeaderCell("界面", 2, 2, 1, 1));
            add(excelExport.new HeaderCell("库容", 3, 2, 1, 1));
            add(excelExport.new HeaderCell("液位", 4, 2, 1, 1));
            add(excelExport.new HeaderCell("界面", 5, 2, 1, 1));
            add(excelExport.new HeaderCell("库容", 6, 2, 1, 1));
            add(excelExport.new HeaderCell("压力", 7, 2, 1, 1));
            add(excelExport.new HeaderCell("温度", 8, 2, 1, 1));
            add(excelExport.new HeaderCell("流量计读数m³", 9, 2, 1, 2));
            add(excelExport.new HeaderCell("油", 10, 2, 2, 1));
            add(excelExport.new HeaderCell("水", 12, 2, 2, 1));
            add(excelExport.new HeaderCell("油", 14, 2, 1, 1));
            add(excelExport.new HeaderCell("油", 15, 2, 1, 1));
            add(excelExport.new HeaderCell("天然气", 16, 2, 1, 1));
            add(excelExport.new HeaderCell("天然气", 17, 2, 1, 1));
            add(excelExport.new HeaderCell("天然气", 18, 2, 1, 1));
            add(excelExport.new HeaderCell("出口汇管", 36, 2, 2, 1));
//行分隔
            add(excelExport.new HeaderCell("m", 1, 3, 1, 1));
            add(excelExport.new HeaderCell("m", 2, 3, 1, 1));
            add(excelExport.new HeaderCell("t", 3, 3, 1, 1));
            add(excelExport.new HeaderCell("m", 4, 3, 1, 1));
            add(excelExport.new HeaderCell("m", 5, 3, 1, 1));
            add(excelExport.new HeaderCell("t", 6, 3, 1, 1));
            add(excelExport.new HeaderCell("MPa", 7, 3, 1, 1));
            add(excelExport.new HeaderCell("℃", 8, 3, 1, 1));
            add(excelExport.new HeaderCell("温度℃", 10, 3, 1, 1));
            add(excelExport.new HeaderCell("压力MPa", 11, 3, 1, 1));
            add(excelExport.new HeaderCell("温度℃", 12, 3, 1, 1));
            add(excelExport.new HeaderCell("压力MPa", 13, 3, 1, 1));
            add(excelExport.new HeaderCell("出口温度℃", 14, 3, 1, 1));
            add(excelExport.new HeaderCell("出口温度℃", 15, 3, 1, 1));
            add(excelExport.new HeaderCell("压力MPa", 16, 3, 1, 1));
            add(excelExport.new HeaderCell("压力MPa", 17, 3, 1, 1));
            add(excelExport.new HeaderCell("压力MPa", 18, 3, 1, 1));
            add(excelExport.new HeaderCell("温度℃", 19, 3, 1, 1));
            add(excelExport.new HeaderCell("压力MPa", 20, 3, 1, 1));
            add(excelExport.new HeaderCell("出口温度℃", 21, 3, 1, 1));
            add(excelExport.new HeaderCell("出口温度℃", 22, 3, 1, 1));
            add(excelExport.new HeaderCell("出口温度℃", 23, 3, 1, 1));
            add(excelExport.new HeaderCell("出口压力MPa", 24, 3, 1, 1));
            add(excelExport.new HeaderCell("流量计读数m³", 25, 3, 1, 1));
            add(excelExport.new HeaderCell("燃气量m³", 26, 3, 1, 1));
            add(excelExport.new HeaderCell("压力MPa", 27, 3, 1, 1));
            add(excelExport.new HeaderCell("温度℃", 28, 3, 1, 1));
            add(excelExport.new HeaderCell("流量计读数m³(瞬时)", 29, 3, 1, 1));
            add(excelExport.new HeaderCell("流量计读数m³(累计)", 30, 3, 1, 1));
            add(excelExport.new HeaderCell("泵压MPa", 31, 3, 1, 1));
            add(excelExport.new HeaderCell("泵压MPa", 32, 3, 1, 1));
            add(excelExport.new HeaderCell("泵压MPa", 33, 3, 1, 1));
            add(excelExport.new HeaderCell("压力MPa", 36, 3, 1, 1));
            add(excelExport.new HeaderCell("温度℃", 37, 3, 1, 1));
            add(excelExport.new HeaderCell("压力MPa", 38, 3, 1, 1));
            add(excelExport.new HeaderCell("温度℃", 39, 3, 1, 1));
            add(excelExport.new HeaderCell("流量m³(累计)", 40, 3, 1, 1));
            add(excelExport.new HeaderCell("流量m³/h(瞬时)", 41, 3, 1, 1));
            add(excelExport.new HeaderCell("温度℃", 42, 3, 1, 1));
            add(excelExport.new HeaderCell("温度℃", 43, 3, 1, 1));
            add(excelExport.new HeaderCell("温度℃", 44, 3, 1, 1));
            add(excelExport.new HeaderCell("温度℃", 45, 3, 1, 1));
        }};
        ExcelExport.LastRowColumnNum lastRowColumnNum = excelExport.addRowsByData(wb, 0, 0, 0, fileName, getColumnMap(), false, reportData, false, extraHeaderCell);
        rowNum = lastRowColumnNum.getRowNum();

        List<ComplexSheetForm> formData = getForm();
        if (formData != null) {
            StringBuilder allNote = new StringBuilder();
            for (ComplexSheetForm res : formData) {
                HSSFRow row = sheet.createRow(rowNum);
                int columnIndex = 0;
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getReport_hour() != null) {
                        cell.setCellValue(res.getReport_hour());
                    }
                    cell.setCellStyle(excelExport.dataStyle(wb));
                    columnIndex++;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("外输量(m³):");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    excelExport.setRegionStyle(sheet, cellRangeAddress, excelExport.headerStyle(wb));
                    columnIndex = columnIndex + 2 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getWsl() != null) {
                        cell.setCellValue(res.getWsl());
                    }
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    excelExport.setRegionStyle(sheet, cellRangeAddress, excelExport.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("燃气量(吨):");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    excelExport.setRegionStyle(sheet, cellRangeAddress, excelExport.headerStyle(wb));
                    columnIndex = columnIndex + 2 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getRql() != null) {
                        cell.setCellValue(res.getRql());
                    }
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    excelExport.setRegionStyle(sheet, cellRangeAddress, excelExport.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("卸油量(吨):");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    excelExport.setRegionStyle(sheet, cellRangeAddress, excelExport.headerStyle(wb));
                    columnIndex = columnIndex + 2 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getXyl() != null) {
                        cell.setCellValue(res.getXyl());
                    }
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    excelExport.setRegionStyle(sheet, cellRangeAddress, excelExport.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("加药量(吨):");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    excelExport.setRegionStyle(sheet, cellRangeAddress, excelExport.headerStyle(wb));
                    columnIndex = columnIndex + 2 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getJyl() != null) {
                        cell.setCellValue(res.getJyl());
                    }
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 1);
                    sheet.addMergedRegion(cellRangeAddress);
                    excelExport.setRegionStyle(sheet, cellRangeAddress, excelExport.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("备注:");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    excelExport.setRegionStyle(sheet, cellRangeAddress, excelExport.headerStyle(wb));
                    columnIndex = columnIndex + 2 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getBz() != null) {
                        cell.setCellValue(res.getBz());
                        allNote.append(res.getBz()).append("  ");
                    }
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 25);
                    sheet.addMergedRegion(cellRangeAddress);
                    excelExport.setRegionStyle(sheet, cellRangeAddress, excelExport.dataStyle(wb));
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
                    cell.setCellStyle(excelExport.headerStyle(wb));
                    columnIndex++;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(allNote.toString());
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 48);
                    sheet.addMergedRegion(cellRangeAddress);
                    excelExport.setRegionStyle(sheet, cellRangeAddress, excelExport.headerStyle(wb));
                }
            }
        }
        excelExport.responseOut(response, wb, fileName);
    }

    private List<ComplexSheetData> getData() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        List<ComplexSheetData> list = new ArrayList<>();
        //        此处实际由数据库获取数据
        list.add(new ComplexSheetData().setReport_time("00:00").setCyg_jm1(123D).setCyg_jm2(1313D));
        list.add(new ComplexSheetData().setReport_time("01:00"));
        list.add(new ComplexSheetData().setReport_time("02:00").setCyg_jm1(24D).setCyg_jm2(253D));
        list.add(new ComplexSheetData().setReport_time("03:00").setCyg_jm1(255D));
        //        添加合计行
        {
            ComplexSheetData sumRow = listCompute.makeComputeRow( list, ComplexSheetData.class, "sum");
            sumRow.setReport_time("合计");
            list.add(sumRow);
        }
        return list;
    }

    private List<ComplexSheetForm> getForm() {
        List<ComplexSheetForm> list = new ArrayList<>();
        //        此处实际由数据库获取数据
        list.add(new ComplexSheetForm().setReportDate("00:00"));
        list.add(new ComplexSheetForm().setReportDate("01:00"));
        list.add(new ComplexSheetForm().setReportDate("02:00"));
        list.add(new ComplexSheetForm().setReportDate("03:00"));
        return list;
    }
}
