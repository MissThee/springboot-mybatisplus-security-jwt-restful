package com.github.base.controller.example.sheet;

import com.alibaba.fastjson.JSONObject;
import com.github.missthee.tool.collectioncompute.CollectionCompute;
import com.github.missthee.tool.collectioncompute.ComputeType;
import com.github.missthee.tool.excel.exports.CellStyleHelper;
import com.github.missthee.tool.excel.exports.direct.CellPoint;
import com.github.missthee.tool.excel.exports.direct.ColumnConfig;
import com.github.missthee.tool.excel.exports.direct.DefaultStyle;
import com.github.missthee.tool.excel.exports.direct.ExcelExport;
import com.github.missthee.tool.excel.template.SimpleCell;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.data.annotation.Transient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RequestMapping("/sheet/complex")
@RestController()
public class ComplexSheet {

    private List<ColumnConfig> getColumnMap() {
        //前端网页数据所需的列，与后台导出excel所需的列统一在此设置
        //前端数据列，取map的id集合
        //excel数据列，取value非空
        List<ColumnConfig> list = new ArrayList<>();
        list.add(new ColumnConfig("id", "").setNoDataBaseColumn());
        list.add(new ColumnConfig("report_time", "1"));
        list.add(new ColumnConfig("cyg_yw1", "1"));
        list.add(new ColumnConfig("cyg_jm1", "1"));
        list.add(new ColumnConfig("cyg_kr1", "1"));
        list.add(new ColumnConfig("cyg_yw2", "1"));
        list.add(new ColumnConfig("cyg_jm2", "1"));
        list.add(new ColumnConfig("cyg_kr2", "1"));
        list.add(new ColumnConfig("pi_1150", "1"));
        list.add(new ColumnConfig("ti_1270", "1"));
        list.add(new ColumnConfig("ft109", "1"));
        list.add(new ColumnConfig("ti_1250", "1"));
        list.add(new ColumnConfig("pi_1090", "1"));
        list.add(new ColumnConfig("ti_1260", "1"));
        list.add(new ColumnConfig("pi_1100", "1"));
        list.add(new ColumnConfig("ti_1230", "1"));
        list.add(new ColumnConfig("ti_1240", "1"));
        list.add(new ColumnConfig("pi_1140", "1"));
        list.add(new ColumnConfig("pi_1110", "1"));
        list.add(new ColumnConfig("pi_1120", "1"));
        list.add(new ColumnConfig("ti_1200", "1"));
        list.add(new ColumnConfig("pi_1070", "1"));
        list.add(new ColumnConfig("jiarelu1", "1"));
        list.add(new ColumnConfig("jiarelu2", "1"));
        list.add(new ColumnConfig("jiarelu3", "1"));
        list.add(new ColumnConfig("rq_ckyl", "1"));
        list.add(new ColumnConfig("rq_ljds", "1"));
        list.add(new ColumnConfig("ranqiliang", "1"));
        list.add(new ColumnConfig("pi_1060", "1"));
        list.add(new ColumnConfig("ti_1150", "1"));
        list.add(new ColumnConfig("ft104s", "1"));
        list.add(new ColumnConfig("ft_1040", "1"));
        list.add(new ColumnConfig("rsb_by1", "1"));
        list.add(new ColumnConfig("rsb_by2", "1"));
        list.add(new ColumnConfig("rsb_by3", "1"));
        list.add(new ColumnConfig("lt_1030", "1"));
        list.add(new ColumnConfig("hsg_wd", "1"));
        list.add(new ColumnConfig("pi_1080", "1"));
        list.add(new ColumnConfig("ti_1210", "1"));
        list.add(new ColumnConfig("pi_1010", "1"));
        list.add(new ColumnConfig("ti_1010", "1"));
        list.add(new ColumnConfig("ft_1010", "1"));
        list.add(new ColumnConfig("ft101s", "1"));
        list.add(new ColumnConfig("cshrq_hgwd", "1"));
        list.add(new ColumnConfig("ti_1020", "1"));
        list.add(new ColumnConfig("ti_1030", "1"));
        list.add(new ColumnConfig("ti_1220", "1"));
        list.add(new ColumnConfig("ws_yl", "1"));
        list.add(new ColumnConfig("ws_wd", "1"));
        list.add(new ColumnConfig("ws_llj", "1"));
        list.add(new ColumnConfig("yeliang", "1").setNoDataBaseColumn());
        return list;
    }

    @PostMapping("/excel")
    public void excel(@RequestBody() JSONObject bodyJO, HttpServletResponse response) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        int rowNum = 0;
        List<ComplexSheetDataModel> reportData = getData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = bodyJO.getDate("searchDate");
        String searchDateStr = sdf.format(searchDate == null ? new Date() : searchDate);
        String fileName = "复杂报表（" + searchDateStr + "）";
        //以下表头格式extraHeaderCell，使用ExcelReadTool类生成，代码打印在控制台，手动复制到本处
        List<SimpleCell> extraHeaderCell = new ArrayList<SimpleCell>();
//行分隔
        extraHeaderCell.add(new SimpleCell("项目", 0, 0, 1, 2));
        extraHeaderCell.add(new SimpleCell("储油罐", 1, 0, 6, 1));
        extraHeaderCell.add(new SimpleCell("外输泵", 7, 0, 3, 1));
        extraHeaderCell.add(new SimpleCell("进站（来液升温）换热器", 10, 0, 6, 1));
        extraHeaderCell.add(new SimpleCell("分离缓冲罐", 16, 0, 3, 1));
        extraHeaderCell.add(new SimpleCell("加热炉", 19, 0, 5, 1));
        extraHeaderCell.add(new SimpleCell("燃气", 24, 0, 3, 1));
        extraHeaderCell.add(new SimpleCell("热水泵", 27, 0, 7, 1));
        extraHeaderCell.add(new SimpleCell("热回水罐", 34, 0, 2, 1));
        extraHeaderCell.add(new SimpleCell("燃油泵", 36, 0, 2, 1));
        extraHeaderCell.add(new SimpleCell("掺水泵", 38, 0, 4, 1));
        extraHeaderCell.add(new SimpleCell("掺水换热器", 42, 0, 3, 1));
        extraHeaderCell.add(new SimpleCell("燃油换热器", 45, 0, 1, 1));
        extraHeaderCell.add(new SimpleCell("外输记录", 46, 0, 4, 1));
//行分隔
        extraHeaderCell.add(new SimpleCell("1#", 1, 1, 3, 1));
        extraHeaderCell.add(new SimpleCell("2#", 4, 1, 3, 1));
        extraHeaderCell.add(new SimpleCell("出口汇管", 7, 1, 3, 1));
        extraHeaderCell.add(new SimpleCell("出口汇管", 10, 1, 4, 1));
        extraHeaderCell.add(new SimpleCell("1#", 14, 1, 1, 1));
        extraHeaderCell.add(new SimpleCell("2#", 15, 1, 1, 1));
        extraHeaderCell.add(new SimpleCell("汇管", 16, 1, 1, 1));
        extraHeaderCell.add(new SimpleCell("1#", 17, 1, 1, 1));
        extraHeaderCell.add(new SimpleCell("2#", 18, 1, 1, 1));
        extraHeaderCell.add(new SimpleCell("出口汇管", 19, 1, 2, 2));
        extraHeaderCell.add(new SimpleCell("1#", 21, 1, 1, 2));
        extraHeaderCell.add(new SimpleCell("2#", 22, 1, 1, 2));
        extraHeaderCell.add(new SimpleCell("3#", 23, 1, 1, 2));
        extraHeaderCell.add(new SimpleCell("#", 24, 1, 3, 2));
        extraHeaderCell.add(new SimpleCell("出口汇管", 27, 1, 4, 2));
        extraHeaderCell.add(new SimpleCell("1#", 31, 1, 1, 2));
        extraHeaderCell.add(new SimpleCell("2#", 32, 1, 1, 2));
        extraHeaderCell.add(new SimpleCell("3#", 33, 1, 1, 2));
        extraHeaderCell.add(new SimpleCell("液位m", 34, 1, 1, 3));
        extraHeaderCell.add(new SimpleCell("温度℃", 35, 1, 1, 3));
        extraHeaderCell.add(new SimpleCell("#", 36, 1, 2, 1));
        extraHeaderCell.add(new SimpleCell("出口汇管", 38, 1, 4, 2));
        extraHeaderCell.add(new SimpleCell("出口汇管", 42, 1, 1, 2));
        extraHeaderCell.add(new SimpleCell("1#", 43, 1, 1, 2));
        extraHeaderCell.add(new SimpleCell("2#", 44, 1, 1, 2));
        extraHeaderCell.add(new SimpleCell("油", 45, 1, 1, 2));
        extraHeaderCell.add(new SimpleCell("压力MPa", 46, 1, 1, 3));
        extraHeaderCell.add(new SimpleCell("温度℃", 47, 1, 1, 3));
        extraHeaderCell.add(new SimpleCell("流量计读数m³", 48, 1, 1, 3));
        extraHeaderCell.add(new SimpleCell("液量m³", 49, 1, 1, 3));
//行分隔
        extraHeaderCell.add(new SimpleCell("时间", 0, 2, 1, 2));
        extraHeaderCell.add(new SimpleCell("液位", 1, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("界面", 2, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("库容", 3, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("液位", 4, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("界面", 5, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("库容", 6, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("压力", 7, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("温度", 8, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("流量计读数m³", 9, 2, 1, 2));
        extraHeaderCell.add(new SimpleCell("油", 10, 2, 2, 1));
        extraHeaderCell.add(new SimpleCell("水", 12, 2, 2, 1));
        extraHeaderCell.add(new SimpleCell("油", 14, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("油", 15, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("天然气", 16, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("天然气", 17, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("天然气", 18, 2, 1, 1));
        extraHeaderCell.add(new SimpleCell("出口汇管", 36, 2, 2, 1));
//行分隔
        extraHeaderCell.add(new SimpleCell("m", 1, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("m", 2, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("t", 3, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("m", 4, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("m", 5, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("t", 6, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("MPa", 7, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("℃", 8, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("温度℃", 10, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("压力MPa", 11, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("温度℃", 12, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("压力MPa", 13, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("出口温度℃", 14, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("出口温度℃", 15, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("压力MPa", 16, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("压力MPa", 17, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("压力MPa", 18, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("温度℃", 19, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("压力MPa", 20, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("出口温度℃", 21, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("出口温度℃", 22, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("出口温度℃", 23, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("出口压力MPa", 24, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("流量计读数m³", 25, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("燃气量m³", 26, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("压力MPa", 27, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("温度℃", 28, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("流量计读数m³(瞬时)", 29, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("流量计读数m³(累计)", 30, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("泵压MPa", 31, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("泵压MPa", 32, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("泵压MPa", 33, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("压力MPa", 36, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("温度℃", 37, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("压力MPa", 38, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("温度℃", 39, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("流量m³(累计)", 40, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("流量m³/h(瞬时)", 41, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("温度℃", 42, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("温度℃", 43, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("温度℃", 44, 3, 1, 1));
        extraHeaderCell.add(new SimpleCell("温度℃", 45, 3, 1, 1));
        CellPoint cellPosittion = ExcelExport.addContext(wb, 0, 0, 0, fileName, getColumnMap(), false, reportData, false, extraHeaderCell);
        rowNum = cellPosittion.getY();

        List<ComplexSheetFormModel> formData = getForm();
        if (formData != null) {
            StringBuilder allNote = new StringBuilder();
            for (ComplexSheetFormModel res : formData) {
                HSSFRow row = sheet.createRow(rowNum);
                int columnIndex = 0;
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    if (res.getReport_hour() != null) {
                        cell.setCellValue(res.getReport_hour());
                    }
                    cell.setCellStyle(DefaultStyle.dataStyle(wb));
                    columnIndex++;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("外输量（m³）:");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    CellStyleHelper.setRegionStyle(sheet, cellRangeAddress, DefaultStyle.headerStyle(wb));
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
                    CellStyleHelper.setRegionStyle(sheet, cellRangeAddress, DefaultStyle.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("燃气量(吨):");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    CellStyleHelper.setRegionStyle(sheet, cellRangeAddress, DefaultStyle.headerStyle(wb));
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
                    CellStyleHelper.setRegionStyle(sheet, cellRangeAddress, DefaultStyle.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("卸油量(吨):");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    CellStyleHelper.setRegionStyle(sheet, cellRangeAddress, DefaultStyle.headerStyle(wb));
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
                    CellStyleHelper.setRegionStyle(sheet, cellRangeAddress, DefaultStyle.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("加药量(吨):");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    CellStyleHelper.setRegionStyle(sheet, cellRangeAddress, DefaultStyle.headerStyle(wb));
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
                    CellStyleHelper.setRegionStyle(sheet, cellRangeAddress, DefaultStyle.dataStyle(wb));
                    columnIndex = columnIndex + 1 + 1;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue("备注:");
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 2);
                    sheet.addMergedRegion(cellRangeAddress);
                    CellStyleHelper.setRegionStyle(sheet, cellRangeAddress, DefaultStyle.headerStyle(wb));
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
                    CellStyleHelper.setRegionStyle(sheet, cellRangeAddress, DefaultStyle.dataStyle(wb));
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
                    cell.setCellStyle(DefaultStyle.headerStyle(wb));
                    columnIndex++;
                }
                {
                    HSSFCell cell = row.createCell(columnIndex);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(allNote.toString());
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + 48);
                    sheet.addMergedRegion(cellRangeAddress);
                    CellStyleHelper.setRegionStyle(sheet, cellRangeAddress, DefaultStyle.headerStyle(wb));
                }
            }
        }
        ExcelExport.exportToResponse(response, wb, fileName + ".xls");
    }

    private List<ComplexSheetDataModel> getData() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        List<ComplexSheetDataModel> list = new ArrayList<>();
        //        此处实际由数据库获取数据
        list.add(new ComplexSheetDataModel().setReport_time("00:00").setCyg_jm1(123D).setCyg_jm2(1313D));
        list.add(new ComplexSheetDataModel().setReport_time("01:00"));
        list.add(new ComplexSheetDataModel().setReport_time("02:00").setCyg_jm1(24D).setCyg_jm2(253D));
        list.add(new ComplexSheetDataModel().setReport_time("03:00").setCyg_jm1(255D));
        //        添加合计行
        {
            ComplexSheetDataModel sumRow = CollectionCompute.buildComputeRow(list, ComplexSheetDataModel.class, ComputeType.AVG);
            sumRow.setReport_time("合计");
            list.add(sumRow);
        }
        return list;
    }

    private List<ComplexSheetFormModel> getForm() {
        List<ComplexSheetFormModel> list = new ArrayList<>();
        //        此处实际由数据库获取数据
        list.add(new ComplexSheetFormModel().setReportDate("00:00"));
        list.add(new ComplexSheetFormModel().setReportDate("01:00"));
        list.add(new ComplexSheetFormModel().setReportDate("02:00"));
        list.add(new ComplexSheetFormModel().setReportDate("03:00"));
        return list;
    }

    @Data
    @Accessors(chain = true)
    public static class ComplexSheetDataModel {
        private Long id;
        private String reportDate;
        private String report_time;
        private Double cyg_yw1;                             //储油罐
        private Double cyg_jm1;
        private Double cyg_kr1;
        private Double cyg_yw2;
        private Double cyg_jm2;
        private Double cyg_kr2;
        private Double pi_1150;                             //外输泵
        private Double ti_1270;
        private Double ft109;
        private Double ti_1250;                             //进站（来液升温）换热器
        private Double pi_1090;
        private Double ti_1260;
        private Double pi_1100;
        private Double ti_1230;
        private Double ti_1240;
        private Double pi_1140;                             //分离缓冲罐
        private Double pi_1110;
        private Double pi_1120;
        private Double ti_1200;                             //加热炉
        private Double pi_1070;
        @Transient
        private Double jiarelu1;
        @Transient
        private Double jiarelu2;
        @Transient
        private Double jiarelu3;
        private Double rq_ckyl;                              //    燃气
        private Double rq_ljds;
        @Transient
        private Double ranqiliang;
        private Double pi_1060;                             //热水泵
        private Double ti_1150;
        private Double ft104s;
        private Double ft_1040;
        private Double rsb_by1;
        private Double rsb_by2;
        private Double rsb_by3;
        private Double lt_1030;                             //热回水罐
        private Double hsg_wd;                             //
        private Double pi_1080;                             //燃油泵
        private Double ti_1210;
        private Double pi_1010;                             //掺水泵
        private Double ti_1010;
        private Double ft_1010;
        private Double ft101s;
        private Double cshrq_hgwd;                             //掺水换热器
        private Double ti_1020;
        private Double ti_1030;
        private Double ti_1220;                             //燃油换热器
        private Double ws_yl;                             //    外输记录
        private Double ws_wd;
        private Double ws_llj;
        @Transient
        private Double yeliang;

    }

    @Data
    @Accessors(chain = true)
    public static class ComplexSheetFormModel {
        private Long id;
        private String reportDate;
        private String report_hour;
        private Short wsl;
        private Short rql;
        private Short xyl;
        private Short jyl;
        private String bz;
        private Short mark;
        private Date stime;

    }
}
