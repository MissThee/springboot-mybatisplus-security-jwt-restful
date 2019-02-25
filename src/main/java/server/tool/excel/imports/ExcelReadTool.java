package server.tool.excel.imports;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import server.tool.excel.template.SimpleCell;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import static server.tool.excel.template.TemplateTool.getSimpleCellInfo;

//excel 文件格式读取工具，用于生成表头代码，供导出Excel使用
public class ExcelReadTool {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("输入excel文件地址（.xls）:");
            String filePath = scanner.next();
            System.out.println("生成结果是否需要位移？（y/n）:");
            String isNeedMove = scanner.next();
            int fixX = 0, fixY = 0;
            if ("y".equals(isNeedMove.toLowerCase())) {
                System.out.println("X偏移数:");
                fixX = scanner.nextInt();
                System.out.println("Y偏移数:");
                fixY = scanner.nextInt();
            }
            excelToDataColumn(WorkbookFactory.create(new File(filePath)), fixX, fixY, true, true);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取excel文件
     *
     * @param fixX 相对读取表格，生成后x轴偏移格数正右负左
     * @param fixY 相对读取表格，生成后y轴偏移格数正下负上
     */
    @SuppressWarnings("all")
    public static String excelToDataColumn(Workbook wb, int fixX, int fixY, boolean needPrintToConsole, boolean needPasteToClipBoard) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("//generate code start\r\n");
        Iterator<Sheet> sheetIterator = wb.sheetIterator();
        int sheetIndex = 0;
        for (Sheet sheet : wb) {
            stringBuilder.append("//Sheet" + sheetIndex++ + "\r\n");
            Iterator<Row> rowIterator = sheet.rowIterator();
            int rowIndex = 0;
            for (Row row : sheet) {
                stringBuilder.append("//Sheet" + sheetIndex + ", Row" + rowIndex++ + "\r\n");
                for (Cell cell : row) {
                    SimpleCell cellInfo = getSimpleCellInfo(sheet, cell);
                    if (cellInfo.getValue() != null) {
                        if ((cellInfo.getX() + fixX) >= 0 && (cellInfo.getY() + fixY) >= 0) {
                            stringBuilder.append("add(new " + SimpleCell.class.getSimpleName() + "(\"" + cellInfo.getValue() + "\"," + (cellInfo.getX() + fixX) + "," + (cellInfo.getY() + fixY) + "," + cellInfo.getW() + "," + cellInfo.getH() + "));\r\n");
                        }
                    }
                }
            }
        }
        stringBuilder.append("//generate code end\r\n");
        String str = stringBuilder.toString();
        if (needPrintToConsole) {
            System.out.println(str);
        }
        if (needPasteToClipBoard) {
            pasteToClipBoard(str);
        }
        return stringBuilder.toString();
    }

    private static void pasteToClipBoard(String str) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        clip.setContents(new StringSelection(str), null);
        System.out.println("【已复制到剪贴板】");
    }
//    private static String getCellValue(Cell cell) {
//        if (cell == null) return null;
//        switch (cell.getCellTypeEnum()) {
//            case NUMERIC:
//            case STRING:
//                return cell.getStringCellValue();
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA:
//                return cell.getCellFormula();
//            default:
//                return "";
//        }
//    }
}
