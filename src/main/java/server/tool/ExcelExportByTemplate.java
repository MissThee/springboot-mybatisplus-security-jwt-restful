package server.tool;


import com.alibaba.fastjson.JSON;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelExportByTemplate {
    /**
     * 读取模板文件，以${xxxXxx}为占位符，按Model中的变量替换值
     *
     * @param templateFilePath 模板文件路径。开头不加/，以resources目录为根目录，如"static/test.xls"
     * @param exportFileName   输出的excel表文件名
     * @param sheetIndex       sheet页下标：从0开始
     * @param t                实体类
     */
    public static <T> void ExportByTemplate(HttpServletResponse response, String templateFilePath, String exportFileName, int sheetIndex, T t) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        HSSFWorkbook wb = new HSSFWorkbook(Objects.requireNonNull(ExcelExportByTemplate.class.getClassLoader().getResourceAsStream(templateFilePath)));
        List<String> cellStyleStrList = new ArrayList<>();
        HSSFSheet sheet = wb.getSheetAt(sheetIndex);
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            for (Cell c : row) {
                if (!cellStyleStrList.contains(JSON.toJSONString(c.getCellStyle()))) {
                    cellStyleStrList.add(JSON.toJSONString(c.getCellStyle()));
                }
                String value = null;
                boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                //判断是否具有合并单元格，取到单元格内容
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
                    }
                } else {
                    c.setCellType(CellType.STRING);
                    value = c.getStringCellValue();
                }
                //替换值
                {
                    String valueStr = String.valueOf(value);

                    Pattern p = Pattern.compile("\\$\\{([0-9a-zA-Z_$]*?)\\}");
                    Matcher m = p.matcher(valueStr);
                    while (m.find()) {

                        String modelPropertyName = m.group(1);
                        Object modelPropertyValue = invokeGetMethod(t, modelPropertyName);
                        c.setCellValue(valueStr.replace(m.group(), String.valueOf(modelPropertyValue)));
                    }
                }
            }

        }
        responseOut(response, wb, exportFileName);
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

    private static void responseOut(HttpServletResponse response, HSSFWorkbook wb, String fileName) throws IOException {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
        response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
        OutputStream outputStream = response.getOutputStream(); // 打开流
        wb.write(outputStream);// HSSFWorkbook写入流
        wb.close();// HSSFWorkbook关闭
        outputStream.flush();// 刷新流
        outputStream.close();// 关闭流
    }

    /**
     * 执行getXxx方法
     *
     * @param t            执行set方法的对象
     * @param propertyName 执行set方法的属性名
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static <T> Object invokeGetMethod(T t, String propertyName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //通过集合类中列举的属性，获取对应的getXxx()方法名
        String getGetMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        //通过方法名获取方法
        Method getMethod = t.getClass().getMethod(getGetMethodName);
        //执行方法获取值
        return getMethod.invoke(t);
    }

    /**
     * 执行setXxx方法
     *
     * @param t             执行set方法的对象
     * @param propertyName  执行set方法的属性名
     * @param setValue      执行set方法的参数
     * @param setValueClazz 参数的类型
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static <T> void invokeSetMethod(T t, String propertyName, Object setValue, Class setValueClazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //通过集合类中列举的属性，获取对应的setXxx()方法名
        String getGetMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        //通过方法名获取方法
        Method setMethod = t.getClass().getMethod(getGetMethodName, setValueClazz);
        //执行方法获设置值
        setMethod.invoke(t, setValue);
    }
}