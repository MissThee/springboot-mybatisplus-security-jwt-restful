package server.tool.excel.exports.bytemplate;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;
import server.tool.excel.template.SimpleCell;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static server.tool.excel.reflection.GetterAndSetter.invokeGetMethod;
import static server.tool.excel.template.TemplateTool.getSimpleCellInfo;
import static server.tool.excel.response.ResponseTool.responseOut;

public class ExcelExportByTemplate {
    /**
     * 读取模板文件，以${xxxXxx}为占位符，按Model中的变量替换值
     *
     * @param sheetIndex sheet页下标：从0开始
     * @param t          实体类
     */
    public static <T> Workbook simplePartialReplaceByPOJO(Workbook wb, int sheetIndex, T t) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Sheet sheet = wb.getSheetAt(sheetIndex);
        for (Row row : sheet) {
            if (row == null) {
                continue;
            }
            for (Cell cell : row) {
                SimpleCell cellInfo = getSimpleCellInfo(sheet, cell);
                if (cellInfo.getValue() != null) {
                    //替换值
                    String valueStr = cellInfo.getValue();
                    Pattern p = Pattern.compile("\\$\\{([0-9a-zA-Z_$]*?)\\}");
                    Matcher m = p.matcher(valueStr);
                    while (m.find()) {
                        String modelPropertyName = m.group(1);
                        Object modelPropertyValue = invokeGetMethod(t, modelPropertyName);
                        valueStr = valueStr.replace(m.group(), String.valueOf(modelPropertyValue));
                        cell.setCellValue(valueStr);
                    }
                }
            }
        }
        return wb;
    }

    public static Workbook readFile(String templateFilePath) throws IOException {
        Workbook wb;
        if (templateFilePath.endsWith(".xls")) {
            wb = new HSSFWorkbook(Objects.requireNonNull(ExcelExportByTemplate.class.getClassLoader().getResourceAsStream(templateFilePath)));
        } else if (templateFilePath.endsWith(".xlsx")) {
            wb = new XSSFWorkbook(Objects.requireNonNull(ExcelExportByTemplate.class.getClassLoader().getResourceAsStream(templateFilePath)));
        } else {
            throw new FileFormatException(" only for .xls or .xlsx ");
        }
        return wb;
    }

    /**
     * 读取模板文件，以$[类名]为占位符，按propertyNameList中的属性名，对应的datalist中的变量替换
     *
     * @param sheetIndex       sheet页下标：从0开始
     * @param dataList         实体类集合
     * @param propertyNameList 实体类属性名集合
     */
    public static <T> Workbook simpleReplaceByPOJOList(Workbook wb, int sheetIndex, List<T> dataList, List<String> propertyNameList,Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Sheet sheet = wb.getSheetAt(sheetIndex);
        for (int rowIndex = 0; rowIndex < sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
                Cell cell = row.getCell(columnIndex);
                if (cell == null) {
                    continue;
                }
                SimpleCell cellInfo = getSimpleCellInfo(sheet, cell);
                if (cellInfo.getValue() != null) {
                    String valueStr = cellInfo.getValue();
                    //用List替换值
                    if (dataList != null && dataList.size() > 0) {
                        if (valueStr.contains("$["+clazz.getSimpleName()+"]")) {
                            int repalceRowIndex = rowIndex;
                            for (T t : dataList) {
                                if (valueStr.contains("$["+clazz.getSimpleName()+"]")) {
                                    int repalceColumnIndex = columnIndex;
                                    for (String propertyName : propertyNameList) {
                                        Object o = invokeGetMethod(t, propertyName);
                                        Row currentRow = sheet.getRow(repalceRowIndex);
                                        if (currentRow == null) {
                                            currentRow = sheet.createRow(repalceRowIndex);
                                        }
                                        Cell currentCell = currentRow.getCell(repalceColumnIndex);
                                        if (currentCell == null) {
                                            currentCell = currentRow.createCell(repalceColumnIndex);
                                        }
                                        currentCell.setCellValue(o.toString());
                                        repalceColumnIndex++;
                                    }
                                }
                                repalceRowIndex++;
//                               取下一行首个值
                                {
                                    Row nextRow = sheet.getRow(repalceRowIndex);
                                    if (nextRow == null) {
                                        valueStr = "";
                                    } else {
                                        Cell nextCell = nextRow.getCell(columnIndex);
                                        if (nextCell == null) {
                                            valueStr = "";
                                        } else {
                                            valueStr = nextCell.getStringCellValue();
                                        }
                                    }
                                }
                                if (repalceRowIndex >= sheet.getLastRowNum()) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return wb;
    }

    public static void export(HttpServletResponse response, Workbook wb, String fileName) throws IOException {
        if (wb.getClass().equals(HSSFWorkbook.class)) {
            fileName += ".xls";
        } else if (wb.getClass().equals(XSSFWorkbook.class)) {
            fileName += ".xlsx";
        }
        responseOut(response, wb, fileName);
    }
}