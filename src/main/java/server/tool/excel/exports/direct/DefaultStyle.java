package server.tool.excel.exports.direct;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class DefaultStyle {
    public static HSSFCellStyle titleStyle(HSSFWorkbook wb) {
        // 设置标题样式
        HSSFCellStyle style = commonStyle(wb);
        // 设置字体样式
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 12); // 字体高度
        font.setFontName("ARIAL"); // 字体样式
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    public static HSSFCellStyle headerStyle(HSSFWorkbook wb) {
        // 设置列名样式
        HSSFCellStyle style = commonStyle(wb);
        // 设置字体样式
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10); // 字体高度
        font.setFontName("ARIAL"); // 字体样式
        font.setBold(true);
        style.setFont(font);

        return style;
    }

    public static HSSFCellStyle dataStyle(HSSFWorkbook wb) {
        // 设置数据样式
        HSSFCellStyle style = commonStyle(wb);
        // 设置数据字体
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10); // 字体高度
        font.setFontName("ARIAL"); // 字体
        style.setFont(font);
        return style;
    }

    private static HSSFCellStyle commonStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        // 设置数据边框
        style.setBorderTop(BorderStyle.THIN);// 上边框 细边线
        style.setBorderBottom(BorderStyle.THIN);// 下边框 细边线
        style.setBorderLeft(BorderStyle.THIN);// 左边框 细边线
        style.setBorderRight(BorderStyle.THIN);// 右边框 细边线
        // 设置居中样式
        style.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        return style;
    }
}