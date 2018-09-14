package server.tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ListCompute {
    /**
     * @Param startIndex 开始行下标
     * @Param listSize reslist的大小
     * @Param resList 提供的计算数据表
     * @Param columnKeys需计算的列名集合
     * @Param resObj接收计算结果的对象
     * @Param computeType默认计算方式[sum]、[avg]
     * @Param computeTypeMap例外列计算方式 put( 列名 , avg/sum );
     * */
    public static <T> void makeComputeRow(int startIndex, int listSize, List<T> resList, Collection<String> columnKeys, T resObj, String computeType, Map<String, String> computeTypeMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, Number> mapAvg = new HashMap<>();
        for (int i = startIndex; i < listSize; i++) {
            for (String key1 : columnKeys) {
                // 利用反射，根据传过来的字段名数组，动态调用对应的getXxx()方法得到属性值
                String getGetMethodName = "get" + key1.substring(0, 1).toUpperCase() + key1.substring(1);// 取得对应getXxx()方法
                Class<?> tGetCls = resList.get(i).getClass();// 泛型为Object以及所有Object的子类
                Method getMethod = tGetCls.getMethod(getGetMethodName);// 通过方法名得到对应的方法
                Object value = getMethod.invoke(resList.get(i));// 动态调用方,得到属性值
                if (value instanceof Long) {
                    long valueLong;
                    try {
                        valueLong = (Long) value;
                    } catch (Exception e) {
                        valueLong = 0;
                    }
                    mapAvg.put(key1, (mapAvg.containsKey(key1) ? (long) mapAvg.get(key1) + valueLong : valueLong));
                    if (i == listSize - 1) {
                        try {
                            // 利用反射，根据传过来的字段名数组，动态调用对应的setXxx()方法得到属性值
                            String getSetMethodName = "set" + key1.substring(0, 1).toUpperCase() + key1.substring(1);// 取得对应getXxx()方法
                            Class<?> tSetCls = resObj.getClass();// 泛型为Object以及所有Object的子类
                            Method setMethod = tSetCls.getMethod(getSetMethodName, Long.class);// 通过方法名得到对应的方法
                            long setValue = (long) mapAvg.get(key1);
                            setValue = (long)getSetValue(startIndex, listSize, computeType, computeTypeMap, key1, setValue);
                            setMethod.invoke(resObj, setValue);
                        } catch (Exception e) {
                            System.out.println("非可累加列，忽略统计值：" + key1);
                        }
                    }
                } else if (value instanceof Double) {
                    double valueDouble;
                    try {
                        valueDouble = (Double) value;
                    } catch (Exception e) {
                        valueDouble = 0;
                    }
                    mapAvg.put(key1, (mapAvg.containsKey(key1) ? (double) mapAvg.get(key1) + valueDouble : valueDouble));
                    if (i == listSize - 1) {
                        try {
                            // 利用反射，根据传过来的字段名数组，动态调用对应的setXxx()方法得到属性值
                            String getSetMethodName = "set" + key1.substring(0, 1).toUpperCase() + key1.substring(1);// 取得对应getXxx()方法
                            Class<?> tSetCls = resObj.getClass();// 泛型为Object以及所有Object的子类
                            Method setMethod = tSetCls.getMethod(getSetMethodName, Double.class);// 通过方法名得到对应的方法
                            double setValue = (double) mapAvg.get(key1);
                            setValue = getSetValue(startIndex, listSize, computeType, computeTypeMap, key1, setValue);
                            setMethod.invoke(resObj, setValue);
                        } catch (Exception e) {
                            System.out.println("非可累加列，忽略统计值：" + key1);
                        }
                    }
                }
            }
        }
    }

    private static double getSetValue(int startIndex, int listSize, String computeType, Map<String, String> computeTypeMap, String key1, double setValue) throws Exception {
        if (computeTypeMap.containsKey(key1)) {
            if (computeTypeMap.get(key1).toLowerCase().equals("sum")) {

            } else if (computeTypeMap.get(key1).toLowerCase().equals("avg")) {
                setValue = setValue / (listSize - startIndex);
            } else {
                throw new Exception("computeTypeMap的value值只能为[sum],[avg]");
            }
        } else {
            if (computeType.toLowerCase().equals("sum")) {

            } else if (computeType.toLowerCase().equals("avg")) {
                setValue = setValue / (listSize - startIndex);
            } else {
                throw new Exception("computeType值只能为[sum],[avg]");
            }
        }
        return setValue;
    }

}
