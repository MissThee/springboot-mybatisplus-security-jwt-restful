package com.github.common.tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MapAndBean {
    public static <T> T mapToBeanForce(Map<String, String> map, T t) throws IllegalAccessException {
        for (String key : map.keySet()) {
            try {
                Method method = t.getClass().getMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), String.class);
                method.invoke(t, map.get(key));
            } catch (NoSuchMethodException | InvocationTargetException ignored) {

            }
        }
        return t;
    }

    public static <T> T mapToBeanForce(Map<String, String> map, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T t = clazz.newInstance();
        return mapToBeanForce(map, t);
    }

    public static <T> T mapToBean(Map<String, String> map, T t) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (String key : map.keySet()) {
            String value = map.get(key);
            for (Method method : t.getClass().getDeclaredMethods()) {
                if (method.getName().equals("set" + key.substring(0, 1).toUpperCase() + key.substring(1))) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (Class<?> parameterType : parameterTypes) {
                        if (parameterType == String.class) {
                            method.invoke(t, value);
                        } else if (parameterType == Integer.class) {
                            method.invoke(t, Integer.parseInt(value));
                        } else if (parameterType == Long.class) {
                            method.invoke(t, Long.parseLong(value));
                        } else {
                            throw new NoSuchMethodException("mapToBean未知的参数类型，需补充转换规则");
                        }
                    }
                }
            }
        }
        return t;
    }

    public static <T> T mapToBean(Map<String, String> map, Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        T t = clazz.newInstance();
        return mapToBean(map, t);
    }
}
