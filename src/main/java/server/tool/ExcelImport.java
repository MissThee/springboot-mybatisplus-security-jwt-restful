package server.tool;

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
     * @Param file 接收的Excel文件
     * @Param clazz 实体类
     * @Param columnMap （表头名称,实体类字段）
     */
    public static <T> List<T> excel2POJOList(MultipartFile file, Class clazz, Map<String, String> columnMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, InvalidFormatException {
        List<T> tList = new ArrayList<>();
        Sheet sheet = WorkbookFactory.create(file.getInputStream()).getSheetAt(0);
        //获取表格首列表头名称
        List<String> columnNameList = new ArrayList<>();
        for (Cell c : sheet.getRow(0)) {
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
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            T t = (T) Class.forName(clazz.getName()).newInstance();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell c = row.getCell(j);
                c.setCellType(CellType.STRING);
                String value = c.getStringCellValue();
                if (value != null) {
                    if (columnMap.containsKey(columnNameList.get(j))) {
                        String columnId = columnMap.get(columnNameList.get(j));
                        columnId = columnId.substring(0, 1).toUpperCase() + columnId.substring(1);
                        //调用方法，准备3个参数
                        String methodName = "set" + columnId;//方法名
                        Class<? > paramClass = Class.forName(methodMap.get(methodName));//参数类型
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


}