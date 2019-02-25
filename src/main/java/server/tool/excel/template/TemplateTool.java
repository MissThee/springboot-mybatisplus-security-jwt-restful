package server.tool.excel.template;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class TemplateTool {
    /**
     * 若单元格为有效值单元格（单个单元格、合并单元的首个单元格），读取单元格
     */
    public static SimpleCell getSimpleCellInfo(Sheet sheet, Cell cell) {
        int x = cell.getColumnIndex();
        int y = cell.getRowIndex();
        int w = 1;
        int h = 1;
        for (CellRangeAddress cellRangeAddress : sheet.getMergedRegions()) {
            int firstX = cellRangeAddress.getFirstColumn();
            int lastX = cellRangeAddress.getLastColumn();
            int firstY = cellRangeAddress.getFirstRow();
            int lastY = cellRangeAddress.getLastRow();
            //判断是否是合并区间的一部分
            if (x >= firstX && x <= lastX && y >= firstY && y <= lastY) {
                //判断是否是合并区间第一起始cell
                if (y == firstY && x == firstX) {
                    cell.setCellType(CellType.STRING);
                    String value = cell.getStringCellValue();
                    x = cell.getColumnIndex();
                    y = cell.getRowIndex();
                    w = lastX - firstX + 1;
                    h = lastY - firstY + 1;
                    return new SimpleCell(value, x, y, w, h);
                }
                return new SimpleCell(null, x, y, w, h);
            }
        }
        cell.setCellType(CellType.STRING);
        String value = cell.getStringCellValue();
        return new SimpleCell(value, x, y, w, h);
    }
}
