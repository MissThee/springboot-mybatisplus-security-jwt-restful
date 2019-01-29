package server.tool;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//简单表格导入工具
@Component
public class ExcelImport {
    /**
     * @Param file              MultipartFile接收的Excel文件
     * @Param clazz             Class实体类
     * @Param columnMap         Map< 表头名称,实体类字>
     * @Param sheetIndex        int表格号，0开始
     * @Param headerRowIndex    int表头行下标，0开始
     * @Param dataRowIndex      int数据行下标，0开始
     */
    public <T> List<T> excel2POJOList(MultipartFile file, Class clazz, Map<String, String> columnMap, int sheetIndex, int headerRowIndex, int dataRowIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
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
                    if (columnMap.containsKey(columnNameList.get(j))) {
                        String columnId = columnMap.get(columnNameList.get(j));
                        columnId = columnId.substring(0, 1).toUpperCase() + columnId.substring(1);
                        //调用方法
                        String methodName = "set" + columnId;//方法名
                        Class<?> paramClass = Class.forName(methodMap.get(methodName));//参数类型
                        Object param;
                        switch (paramClass.getSimpleName()) {//参数类型判断，并转换
                            default:
                            case "String":
                                param = value;
                                break;
                            case "Integer":
                                param = Integer.parseInt(value);
                                break;
                            case "Double":
                                param = Double.parseDouble(value);
                                break;
                        }
                        t.getClass().getMethod(methodName, paramClass).invoke(t, param);
                    }
                }
            }
            if (i != 0) {
                tList.add(t);
            }
        }
        return tList;
    }

    public <T> List<T> excel2POJOList(MultipartFile file, Class clazz, Map<String, String> columnMap, int sheetIndex, int headerRowIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnMap, sheetIndex, headerRowIndex, headerRowIndex + 1);
    }

    public <T> List<T> excel2POJOList(MultipartFile file, Class clazz, Map<String, String> columnMap, int sheetIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnMap, sheetIndex, 0, 1);
    }

    public <T> List<T> excel2POJOList(MultipartFile file, Class clazz, Map<String, String> columnMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnMap, 0, 0, 1);
    }

    /**
     * @Param file              MultipartFile接收的Excel文件
     * @Param clazz             Class实体类
     * @Param columnList        ArrayList 实体类属性名,按列依次对应
     * @Param sheetIndex        int表格号，0开始
     * @Param headerRowIndex    int表头行下标，0开始
     * @Param dataRowIndex      int数据行下标，0开始
     */
    public <T> List<T> excel2POJOList(MultipartFile file, Class clazz, List<String> columnList, int sheetIndex, int headerRowIndex, int dataRowIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
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
                    String columnId = columnList.get(j);
                    if (columnId == null || "".equals(columnId)) {
                        continue;
                    }
                    columnId = columnId.substring(0, 1).toUpperCase() + columnId.substring(1);
                    //调用方法
                    String methodName = "set" + columnId;//方法名
                    Class<?> paramClass = Class.forName(methodMap.get(methodName));//参数类型
                    Object param;
                    switch (paramClass.getSimpleName()) {//参数类型判断，并转换
                        default:
                        case "String":
                            param = value;
                            break;
                        case "Integer":
                            param = Integer.parseInt(value);
                            break;
                        case "Double":
                            param = Double.parseDouble(value);
                            break;
                    }
                    t.getClass().getMethod(methodName, paramClass).invoke(t, param);
                }
            }
            if (i != 0) {
                tList.add(t);
            }
        }
        return tList;
    }

    public <T> List<T> excel2POJOList(MultipartFile file, Class clazz, List<String> columnList, int sheetIndex, int headerRowIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnList, sheetIndex, headerRowIndex, headerRowIndex + 1);
    }

    public <T> List<T> excel2POJOList(MultipartFile file, Class clazz, List<String> columnList, int sheetIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnList, sheetIndex, 0, 1);
    }

    public <T> List<T> excel2POJOList(MultipartFile file, Class clazz, List<String> columnList) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        return excel2POJOList(file, clazz, columnList, 0, 0, 1);
    }

}