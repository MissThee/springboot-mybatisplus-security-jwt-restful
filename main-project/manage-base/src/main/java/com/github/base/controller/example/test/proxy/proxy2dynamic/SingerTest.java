package com.github.base.controller.example.test.proxy.proxy2dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//2.动态代理，JDK代理。目标对象必须实现一个或多个接口
public class SingerTest {
    public static void main(String[] args) {
        ISinger TARGET = new Singer();
        ISinger PROXY = (ISinger) Proxy.newProxyInstance(
                TARGET.getClass().getClassLoader(),     //ClassLoader loader        目标对象使用类加载器，写法固定
                TARGET.getClass().getInterfaces(),      //Class<?>[] interfaces     目标对象的接口的类型，写法固定
                new InvocationHandler() {               //InvocationHandler h       事件处理接口，需传入一个实现类，一般直接使用匿名实现类
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("开幕");
//                        System.out.println("proxy: " + proxy);//JDK生成的代理对象//不能输出
//                        System.out.println("method: " + method);//原方法
//                        System.out.println("args: " + Arrays.toString(args));//原方法入参
                        Object returnValue = method.invoke(TARGET, args);//执行原方法
                        System.out.println("闭幕");
                        return returnValue;
                    }
                });
        PROXY.sing();
    }
}