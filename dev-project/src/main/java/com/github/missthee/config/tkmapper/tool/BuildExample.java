package com.github.missthee.config.tkmapper.tool;

import tk.mybatis.mapper.entity.Example;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//使用接收参数的对象中的属性构建where和orderBy语句
//requestParam对象格式要求：
// 1.其中出现的属性，需与查询的实体类中的属性同名
// 2.orderBy属性为LinkedHashMap类型时才可使用其构建order by语句，否则按照普通字段处理
public class BuildExample {
    public static <T> Example build(Class modelClass, T requestParam) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException {
        boolean hasOrderBy = false;
        Example example = new Example(modelClass);
        Example.Criteria criteria = example.createCriteria();
        Class<?> aClass = requestParam.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        //拼装where语句
        List<String> modelClassPropertyList = Arrays.stream(modelClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

        for (Field field : declaredFields) {
            String fieldName = field.getName();
            //orderBy为约定Map类型参数，作为排序使用
            if (fieldName.equals("orderBy") && field.getType().equals(LinkedHashMap.class)) {
                hasOrderBy = true;
                continue;
            }
            if (!modelClassPropertyList.contains(fieldName)) {
                throw new NoSuchFieldException("[" + fieldName + "] not exist in Model");
            }
            Object valueObj = aClass.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(requestParam);
            //如果这个属性取值为null或空字符串，则忽略
            if (valueObj == null || valueObj.toString().isEmpty()) {
                continue;
            }
            String valueStr = String.valueOf(valueObj);
            //startDate为约定时间范围查询开始节点
            if (fieldName.equals("startDate")) {
                criteria.andGreaterThanOrEqualTo(fieldName, valueStr);
                //endDate为约定时间范围查询结束节点
            } else if (fieldName.equals("endDate")) {
                criteria.andLessThan(fieldName, valueStr);
                //如果字段为字符串，默认使用模糊匹配查询。并将接收的字符串中的通配符进行转义
            } else if (field.getType() == String.class) {
                criteria.andLike(fieldName, "%" + valueStr.replace("_", "\\_").replace("%", "\\%") + "%");
                //其他类型则使用相等判断
            } else {
                criteria.andEqualTo(fieldName, valueObj);
            }
        }

        if (hasOrderBy) {
            Map<String, String> orderByMap = (Map<String, String>) aClass.getMethod("getOrderBy").invoke(requestParam);
            if (orderByMap != null && !orderByMap.isEmpty()) {
                StringBuilder orderBySB = new StringBuilder();
                for (Map.Entry<String, String> orderByEntry : orderByMap.entrySet()) {
                    String propertyName = String.valueOf(orderByEntry.getKey());
                    if (!modelClassPropertyList.contains(propertyName)) {
                        throw new NoSuchFieldException("[" + propertyName + "] not exist in Table");
                    }
                    String ascOrDesc = orderByEntry.getValue();
                    if (!ascOrDesc.toLowerCase().equals("asc") && !ascOrDesc.toLowerCase().equals("desc")) {
                        throw new InvalidAttributeValueException("value of [" + propertyName + "], must 'asc' or 'desc'");
                    }

                    StringBuffer columnName = ColumnNameFormatter.underline(propertyName);
                    orderBySB.append(columnName).append(" ").append(ascOrDesc).append(",");
                }
                example.setOrderByClause(orderBySB.deleteCharAt(orderBySB.length() - 1).toString());
            }
        }
        return example;
    }
}
