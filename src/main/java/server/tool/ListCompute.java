package server.tool;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
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
    public <T> T makeComputeRow(int startIndex, int endIndex, List<T> dataList, Collection<String> columnKeys, Class<T> clazz, String computeType, Map<String, String> computeTypeMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        T t = (T) Class.forName(clazz.getName()).newInstance();
        Map<String, Number> computeMap = new HashMap<>();
        //行数。防止溢出，最大为数据size
        endIndex = Math.min(dataList.size(), endIndex);
        for (int i = startIndex; i < endIndex; i++) {
            for (String key1 : columnKeys) {
                Object value = invokeGetMethod(key1, dataList.get(i));
                if (value != null) {
                    if (value instanceof Long) {
                        long valueLong = (Long) value;
                        computeMap.put(key1, (computeMap.containsKey(key1) ? (long) computeMap.get(key1) + valueLong : valueLong));
                        try {
                            long setValue = (long) computeMap.get(key1);
                            setValue = (long) getSetValue(startIndex, endIndex, computeType, computeTypeMap, key1, setValue);
                            invokeSetMethod(key1, t, setValue, Long.class);
                        } catch (Exception e) {
                        }
                    } else if (value instanceof Double) {
                        double valueDouble = (Double) value;
                        computeMap.put(key1, (computeMap.containsKey(key1) ? compute((double) computeMap.get(key1), valueDouble, "+") : valueDouble));
                        try {
                            double setValue = (double) computeMap.get(key1);
                            setValue = getSetValue(startIndex, endIndex, computeType, computeTypeMap, key1, setValue);
                            invokeSetMethod(key1, t, setValue, Double.class);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return t;
    }

    public <T> T makeComputeRow(List<T> dataList, Class<T> clazz, String computeType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        return makeComputeRow(0, dataList.size(), dataList, getObjectAttributeNames(clazz), clazz, computeType, new HashMap<>());
    }

    private double getSetValue(int startIndex, int endIndex, String computeType, Map<String, String> computeTypeMap, String key1, double setValue) throws Exception {
        if (computeTypeMap.containsKey(key1)) {
            if (computeTypeMap.get(key1).toLowerCase().equals("sum")) {

            } else if (computeTypeMap.get(key1).toLowerCase().equals("avg")) {
                setValue = compute(setValue, (endIndex - startIndex), "/");
            } else {
                throw new Exception("computeTypeMap的value值只能为[sum],[avg]");
            }
        } else {
            if (computeType.toLowerCase().equals("sum")) {

            } else if (computeType.toLowerCase().equals("avg")) {
                setValue = compute(setValue, (endIndex - startIndex), "/");
            } else {
                throw new Exception("computeType值只能为[sum],[avg]");
            }
        }
        return setValue;
    }

    /**
     * 获取对象的所有属性值
     *
     * @param clazzT
     * @param <T>
     * @return
     */
    public static <T> List<String> getObjectAttributeNames(Class<T> clazzT) {
        List<String> list = new LinkedList<>();
        try {
            //根据类名获得其对应的Class对象 写上你想要的类名就是了 注意是全名 如果有包的话要加上 比如java.Lang.String
            Class clazz = Class.forName(clazzT.getName());
            //根据Class对象获得属性 私有的也可以获得
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if (f.getType() == long.class || f.getType() == Long.class || f.getType() == double.class || f.getType() == Double.class) {
                    list.add(f.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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

    private <T> Object invokeGetMethod(String propertyName, T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //通过集合类中列举的属性，获取对应的getXxx()方法名
        String getGetMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        //通过方法名获取方法
        Method getMethod = t.getClass().getMethod(getGetMethodName);
        //执行方法获取值
        return getMethod.invoke(t);
    }

    private <T> void invokeSetMethod(String propertyName, T t, Object setValue, Class setValueClazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //通过集合类中列举的属性，获取对应的setXxx()方法名
        String getGetMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        //通过方法名获取方法
        Method setMethod = t.getClass().getMethod(getGetMethodName, setValueClazz);
        //执行方法获设置值
        setMethod.invoke(t, setValue);
    }
}
