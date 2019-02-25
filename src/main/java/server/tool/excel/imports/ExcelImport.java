package server.tool.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//简单表格导入工具
public class ExcelImport {
    /**
     * @param file           MultipartFile接收的Excel文件
     * @param clazz          Class实体类
     * @param columnMap      Map&lt;表头名称,实体类属性名&gt;
     * @param sheetIndex     int表格号，0开始
     * @param headerRowIndex int表头行下标，0开始
     * @param dataRowIndex   int数据行下标，0开始
     */
    public static <T> List<T> excel2POJOList(MultipartFile file, Class clazz, Map<String, String> columnMap, int sheetIndex, int headerRowIndex, int dataRowIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnMap, false, sheetIndex, headerRowIndex, dataRowIndex);
    }

    /**
     * @param file           MultipartFile接收的Excel文件
     * @param clazz          Class实体类
     * @param columnMap      Map&lt;表头名称,实体类属性名&gt;
     * @param sheetIndex     int表格号，0开始
     * @param headerRowIndex int表头行下标，0开始
     */
    public static <T> List<T> excel2POJOList(MultipartFile file, Class clazz, Map<String, String> columnMap, int sheetIndex, int headerRowIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnMap, sheetIndex, headerRowIndex, headerRowIndex + 1);
    }

    /**
     * @param file       MultipartFile接收的Excel文件
     * @param clazz      Class实体类
     * @param columnMap  Map&lt;表头名称,实体类属性名&gt;
     * @param sheetIndex int表格号，0开始
     */
    public static <T> List<T> excel2POJOList(MultipartFile file, Class clazz, Map<String, String> columnMap, int sheetIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnMap, sheetIndex, 0, 1);
    }

    /**
     * @param file      MultipartFile接收的Excel文件
     * @param clazz     Class实体类
     * @param columnMap Map&lt;表头名称,实体类属性名&gt;
     */
    public static <T> List<T> excel2POJOList(MultipartFile file, Class clazz, Map<String, String> columnMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnMap, 0, 0, 1);
    }

    /**
     * @param file           MultipartFile接收的Excel文件
     * @param clazz          Class实体类
     * @param columnList     List&lt;实体类属性名&gt;
     * @param sheetIndex     int表格号，0开始
     * @param headerRowIndex int表头行下标，0开始
     * @param dataRowIndex   int数据行下标，0开始
     */
    public static <T> List<T> excel2POJOList(MultipartFile file, Class clazz, List<String> columnList, int sheetIndex, int headerRowIndex, int dataRowIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        Map<String, String> columnMap = new HashMap<>();
        for (int i = 0; i < columnList.size(); i++) {
            columnMap.put(String.valueOf(i), columnList.get(i));
        }
        return excel2POJOList(file, clazz, columnMap, true, sheetIndex, headerRowIndex, dataRowIndex);
    }

    /**
     * @param file           MultipartFile接收的Excel文件
     * @param clazz          Class实体类
     * @param columnList     List&lt;实体类属性名&gt;
     * @param sheetIndex     int表格号，0开始
     * @param headerRowIndex int表头行下标，0开始
     */
    public static <T> List<T> excel2POJOList(MultipartFile file, Class clazz, List<String> columnList, int sheetIndex, int headerRowIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnList, sheetIndex, headerRowIndex, headerRowIndex + 1);
    }

    /**
     * @param file       MultipartFile接收的Excel文件
     * @param clazz      Class实体类
     * @param columnList List&lt;实体类属性名&gt;
     * @param sheetIndex int表格号，0开始
     */
    public static <T> List<T> excel2POJOList(MultipartFile file, Class clazz, List<String> columnList, int sheetIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnList, sheetIndex, 0, 1);
    }

    /**
     * @param file       MultipartFile接收的Excel文件
     * @param clazz      Class实体类
     * @param columnList List&lt;实体类属性名&gt;
     */
    public static <T> List<T> excel2POJOList(MultipartFile file, Class clazz, List<String> columnList) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnList, 0, 0, 1);
    }

    /**
     * @param file           MultipartFile接收的Excel文件
     * @param clazz          Class实体类
     * @param columnMap      Map&lt;表头名称,实体类属性名&gt;
     * @param columnByIndex  是否以直接以顺序匹配
     * @param sheetIndex     int表格号，0开始
     * @param headerRowIndex int表头行下标，0开始
     * @param dataRowIndex   int数据行下标，0开始
     */
    private static <T> List<T> excel2POJOList(MultipartFile file, Class clazz, Map<String, String> columnMap, Boolean columnByIndex, int sheetIndex, int headerRowIndex, int dataRowIndex) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException, NoSuchMethodException {
        List<T> tList = new ArrayList<>();
        Sheet sheet = WorkbookFactory.create(file.getInputStream()).getSheetAt(sheetIndex);

        //获取表格首列表头名称
        List<String> columnNameList = new ArrayList<>();
        for (Cell c : sheet.getRow(headerRowIndex)) {
            c.setCellType(CellType.STRING);
            columnNameList.add(c.getStringCellValue());
        }

        //获取实体类所有setXxx方法及其参数类型
        Map<String, String> methodMap = new HashMap<>();//  Map<set方法名,参数类型>
        Method[] ms = Class.forName(clazz.getName()).getMethods();
        for (Method m : ms) {
            String methodName = m.getName();
            if (methodName.startsWith("set")) {
                methodMap.put(methodName, m.getParameterTypes()[0].getName());
            }
        }
        //遍历Excel除表头外的数据
        for (int i = dataRowIndex; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            T t = (T) Class.forName(clazz.getName()).newInstance();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell c = row.getCell(j);
                if (c == null) {
                    continue;
                }
                c.setCellType(CellType.STRING);
                String value = c.getStringCellValue();
                if (value != null && !"".equals(value)) {
                    String columnId;
                    if (columnByIndex) {
                        columnId = columnMap.get(String.valueOf(j));
                        if (columnId == null || "".equals(columnId)) {
                            continue;
                        }
                    } else {
                        if (!columnMap.containsKey(columnNameList.get(j))) {
                            continue;
                        }
                        columnId = columnMap.get(columnNameList.get(j));
                    }
                    columnId = columnId.substring(0, 1).toUpperCase() + columnId.substring(1);
                    //调用方法
                    String methodName = "set" + columnId;//方法名
                    Class<?> propertyClass = Class.forName(methodMap.get(methodName));//参数类型
                    Object param;
                    //参数类型判断，并转换
                    if("String".equals(propertyClass.getSimpleName())){
                        param=value;
                    }else{
                        Method method = propertyClass.getMethod("parse" + propertyClass.getSimpleName(), propertyClass);
                        param = method.invoke(propertyClass, value);
                    }
                    t.getClass().getMethod(methodName, propertyClass).invoke(t, param);
                }
            }
            if (i != 0) {
                tList.add(t);
            }
        }
        return tList;
    }
}