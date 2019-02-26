package server.controller.example;

import lombok.Data;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import server.tool.excel.exports.ExcelExportByTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class test {
    public static void main(String[] args) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Workbook wb = ExcelExportByTemplate.readFile("exceltemplate/test.xls");

        TestModel testModel = new TestModel();
        ExcelExportByTemplate.simplePartialReplaceByPOJO(wb, 0, testModel);

        List<DataModel> dataModelList = new ArrayList<>();
        dataModelList.add(new DataModel());
        dataModelList.add(new DataModel());
        dataModelList.add(new DataModel());
        dataModelList.add(new DataModel());
        List<String> propertyList = new ArrayList<>();
        propertyList.add("tianQi");
        propertyList.add("wenDu");
        propertyList.add("fengLi");
        ExcelExportByTemplate.simpleReplaceByPOJOList(wb, 0, dataModelList, propertyList);
        output(wb);
    }

    private static Workbook test() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();//创建表
        HSSFSheet sheet = hssfWorkbook.createSheet();//工作簿
        HSSFRow row = sheet.createRow(0);//行
        HSSFCell cell = row.createCell(0);//单元格

        //增加合并单元格
        CellRangeAddress cellRangeAddress = new CellRangeAddress(1, 5, 1, 3);
        sheet.addMergedRegion(cellRangeAddress);

        //添加样式
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        hssfCellStyle.setBorderTop(BorderStyle.THIN);
        hssfCellStyle.setBorderBottom(BorderStyle.THIN);
        hssfCellStyle.setBorderLeft(BorderStyle.THIN);
        hssfCellStyle.setBorderRight(BorderStyle.THIN);
        cell.setCellStyle(hssfCellStyle);

        cell.setCellValue("填内容");

        HSSFRow row1 = sheet.createRow(10);//行
        HSSFCell cell1 = row1.createCell(10);//单元格
        cell1.setCellValue("测试文字");

        return hssfWorkbook;
    }

    private static void output(Workbook Workbook) {
        try {
            String fileName = "H:/" + UUID.randomUUID().toString() + ".xls";
            FileOutputStream fout = new FileOutputStream(fileName);
            Workbook.write(fout);
            fout.close();
            Workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class TestModel {
        private String test1 = "测试文字1";
        private String test2 = "测试文字2";
        private String test3 = "测试文字3";
    }

    @Data
    public static class DataModel {
        private String tianQi = "天气良好";
        private String wenDu = "温度适中";
        private String fengLi = "1级风";
    }
}
