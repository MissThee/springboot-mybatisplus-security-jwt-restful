package server.tool.excel.exports.direct;

import org.apache.poi.ss.usermodel.*;

public class DefaultStyle {
    public static CellStyle titleStyle(Workbook wb) {
        // 设置标题样式
        CellStyle style = commonStyle(wb);
        // 设置字体样式
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 12); // 字体高度
        font.setFontName("ARIAL"); // 字体样式
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    public static CellStyle headerStyle(Workbook wb) {
        // 设置列名样式
        CellStyle style = commonStyle(wb);
        // 设置字体样式
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10); // 字体高度
        font.setFontName("ARIAL"); // 字体样式
        font.setBold(true);
        style.setFont(font);

        return style;
    }

    public static CellStyle dataStyle(Workbook wb) {
        // 设置数据样式
        CellStyle style = commonStyle(wb);
        // 设置数据字体
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10); // 字体高度
        font.setFontName("ARIAL"); // 字体
        style.setFont(font);
        return style;
    }

    private static CellStyle commonStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
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