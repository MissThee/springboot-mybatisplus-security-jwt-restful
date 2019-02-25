package server.tool.excel;


import com.alibaba.fastjson.JSON;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static server.tool.excel.CellTool.getSimpleCellInfo;
import static server.tool.excel.response.ResponseTool.responseOut;

public class ExcelExportByTemplate {
    /**
     * 读取模板文件，以${xxxXxx}为占位符，按Model中的变量替换值
     *
     * @param templateFilePath 模板文件路径。开头不加/，以resources目录为根目录，如"static/test.xls"对应src/main/resources/static/test.xls文件
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
            for (Cell cell : row) {
                if (!cellStyleStrList.contains(JSON.toJSONString(cell.getCellStyle()))) {
                    cellStyleStrList.add(JSON.toJSONString(cell.getCellStyle()));
                }
                SimpleCell cellInfo = getSimpleCellInfo(sheet, cell);
                if (cellInfo.getValue() != null) {
                    //替换值
                    String valueStr = cellInfo.getValue();
                    Pattern p = Pattern.compile("\\$\\{([0-9a-zA-Z_$]*?)\\}");
                    Matcher m = p.matcher(valueStr);
                    while (m.find()) {
                        String modelPropertyName = m.group(1);
                        Object modelPropertyValue = invokeGetMethod(t, modelPropertyName);
                        cell.setCellValue(valueStr.replace(m.group(), String.valueOf(modelPropertyValue)));
                    }
                }

            }

        }
        responseOut(response, wb, exportFileName + ".xls");
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