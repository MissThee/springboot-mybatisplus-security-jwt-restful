package server.tool.Independent;


import java.io.File;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;

//excel 文件格式读取工具，用于生成表头代码，供导出Excel使用
public class ExcelReadTool {
    public static void main(String[] args) {
        try {
            String filePath = "H:\\raoyang.xls";
            int sheetIndex = 1;
            int startReadLine = 0;
            int endReadLine = 2;
            int fixX = 0;
            int fixY = 0;
            readExcel(WorkbookFactory.create(new File(filePath)), sheetIndex, startReadLine, endReadLine, fixX, fixY);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取excel文件
     *
     * @param sheetIndex    sheet页下标：从0开始
     * @param startReadLine 开始读取的行:从0开始
     * @param endReadLine   最后读取的行(若超过表格实际内容行数，则自动取实际行数)
     * @param fixX          相对读取表格，生成后x轴偏移格数正右负左
     * @param fixY          相对读取表格，生成后y轴偏移格数正下负上
     */
    private static void readExcel(Workbook wb, int sheetIndex, int startReadLine, int endReadLine, int fixX, int fixY) {
        Sheet sheet = wb.getSheetAt(sheetIndex);
        Row row;
        boolean hasRow = false;
        for (int i = startReadLine; i <= sheet.getLastRowNum() && i <= endReadLine; i++) {
            row = sheet.getRow(i);
            if (hasRow) {
                System.out.println("//行分隔");
            }
            hasRow = false;
            for (Cell c : row) {
                String value = null;
                int x = -1;
                int y = -1;
                int h = 1;
                int w = 1;
                boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                //判断是否具有合并单元格
                if (isMerge) {
                    CellRangeAddress cellRangeAddress = null;
                    for (CellRangeAddress cellRangeAddressTemp : sheet.getMergedRegions()) {
                        if (cellRangeAddressTemp.getFirstColumn() == c.getColumnIndex() && cellRangeAddressTemp.getFirstRow() == c.getRowIndex()) {
                            cellRangeAddress = cellRangeAddressTemp;
                            break;
                        }
                    }
                    if (cellRangeAddress != null) {
                        value = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                        x = c.getColumnIndex();
                        y = c.getRowIndex();
                        h = cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow() + 1;
                        w = cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn() + 1;
                    }
                } else {
                    c.setCellType(CellType.STRING);
                    value = c.getStringCellValue();
                    x = c.getColumnIndex();
                    y = c.getRowIndex();
                }
                if (value != null) {
                    if ((x + fixX) >= 0 && (y + fixY) >= 0) {
                        hasRow = true;
                        System.out.println("add(new ExcelUtils.HeaderCell(\"" + value + "\"," + (x + fixX) + "," + (y + fixY) + "," + w + "," + h + "));");
                    }
                }
            }

        }
    }

    /**
     * 获取合并单元格的值
     */
    private static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }
        return null;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     */
    private static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}