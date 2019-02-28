package com.github.missthee.config.log.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GetterAndSetter {
    /**
     * @apiNote 执行getXxx方法
     *
     * @param t            执行set方法的对象
     * @param propertyName 执行set方法的属性名
     */
    protected static <T> Object invokeGetMethod(T t, String propertyName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //通过集合类中列举的属性，获取对应的getXxx()方法名
        String getGetMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        //通过方法名获取方法
        Method getMethod = t.getClass().getMethod(getGetMethodName);
        //执行方法获取值
        return getMethod.invoke(t);
    }

    /**
     * @apiNote 执行setXxx方法
     *
     * @param t             执行set方法的对象
     * @param propertyName  执行set方法的属性名
     * @param setValue      执行set方法的参数
     * @param setValueClazz 参数的类型
     */
    protected static <T> void invokeSetMethod(T t, String propertyName, Object setValue, Class setValueClazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //通过集合类中列举的属性，获取对应的setXxx()方法名
        String getGetMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        //通过方法名获取方法
        Method setMethod = t.getClass().getMethod(getGetMethodName, setValueClazz);
        //执行方法获设置值
        setMethod.invoke(t, setValue);
    }
}
