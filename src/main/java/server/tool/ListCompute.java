package server.tool;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

//list中实体对象，指定字段平均、合计计算工具
@Component
public class ListCompute {
    /**
     * 为List< Model>增加合计/平均行
     *
     * @Param startIndex        开始行下标，0开始
     * @Param listSize          结束下标，超过dataList.size时，为dataList.size
     * @Param datalist          计算的数据源
     * @Param columnKeys        需计算的属性名集合
     * @Param clazz             list中泛型T的.class
     * @Param computeType       全局计算方式[sum]、[avg]
     * @Param computeTypeMap    例外属性计算方式，此属性会按指定方式计算 Map< 列名,"avg"/"sum">;
     */
    public <T> T makeComputeRow(int startIndex, int endIndex, List<T> dataList, Collection<String> columnKeys, Class clazz, String computeType, Map<String, String> computeTypeMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        T t = (T) Class.forName(clazz.getName()).newInstance();
        Map<String, Number> mapAvg = new HashMap<>();
        endIndex = Math.min(dataList.size(), endIndex);
        for (int i = startIndex; i < endIndex; i++) {
            for (String key1 : columnKeys) {
                // 利用反射，根据传过来的字段名数组，动态调用对应的getXxx()方法得到属性值
                String getGetMethodName = "get" + key1.substring(0, 1).toUpperCase() + key1.substring(1);// 取得对应getXxx()方法
                Class<?> tGetCls = dataList.get(i).getClass();// 泛型为Object以及所有Object的子类
                Method getMethod = tGetCls.getMethod(getGetMethodName);// 通过方法名得到对应的方法
                Object value = getMethod.invoke(dataList.get(i));// 动态调用方,得到属性值
                if (value instanceof Double || value instanceof Long) {
                    double valueDouble;
                    try {
                        valueDouble = Double.parseDouble(value.toString());
                    } catch (Exception e) {
                        valueDouble = 0;
                    }
                    mapAvg.put(key1, (mapAvg.containsKey(key1) ? compute((double) mapAvg.get(key1), valueDouble, "+") : valueDouble));
                }
                if (i == endIndex - 1) {
                    try {
                        // 利用反射，根据传过来的字段名数组，动态调用对应的setXxx()方法得到属性值
                        String getSetMethodName = "set" + key1.substring(0, 1).toUpperCase() + key1.substring(1);// 取得对应getXxx()方法
                        Class<?> tSetCls = t.getClass();// 泛型为Object以及所有Object的子类
                        Method setMethod = tSetCls.getMethod(getSetMethodName, Double.class);// 通过方法名得到对应的方法
                        double setValue = (double) mapAvg.get(key1);
                        setValue = getSetValue(startIndex, endIndex, computeType, computeTypeMap, key1, setValue);
                        setMethod.invoke(t, setValue);
                    } catch (Exception e) {
                        System.out.println("非可累加列，忽略统计值：" + key1);
                    }
                }
            }
        }
        return t;
    }

    private double getSetValue(int startIndex, int listSize, String computeType, Map<String, String> computeTypeMap, String key1, double setValue) throws Exception {
        if (computeTypeMap.containsKey(key1)) {
            if (computeTypeMap.get(key1).toLowerCase().equals("sum")) {

            } else if (computeTypeMap.get(key1).toLowerCase().equals("avg")) {
                setValue = compute(setValue, (listSize - startIndex), "/");
            } else {
                throw new Exception("computeTypeMap的value值只能为[sum],[avg]");
            }
        } else {
            if (computeType.toLowerCase().equals("sum")) {

            } else if (computeType.toLowerCase().equals("avg")) {
                setValue = compute(setValue, (listSize - startIndex), "/");
            } else {
                throw new Exception("computeType值只能为[sum],[avg]");
            }
        }
        return setValue;
    }

    private double compute(double numA, double numB, String operate) {
        double res = 0;
        BigDecimal bigA = new BigDecimal(Double.toString(numA));
        BigDecimal bigB = new BigDecimal(Double.toString(numB));
        switch (operate) {

            case "+":
                res = bigA.add(bigB).doubleValue();
                break;
            case "-":
                res = bigA.subtract(bigB).doubleValue();
                break;
            case "*":
                res = bigA.multiply(bigB).doubleValue();
                break;
            case "/":
                res = bigA.divide(bigB, 2, RoundingMode.HALF_EVEN).doubleValue();
                break;
            default:
                System.out.println("运算符不合法~");
                break;
        }
        return res;
    }
}
