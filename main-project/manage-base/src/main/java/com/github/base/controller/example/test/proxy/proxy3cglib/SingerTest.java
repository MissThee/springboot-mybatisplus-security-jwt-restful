package com.github.base.controller.example.test.proxy.proxy3cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

//3.cglib代理
public class SingerTest {
    public static void main(String[] args) {
        //目标对象
        Singer TARGET = new Singer();
        //代理对象
        Enhancer en = new Enhancer() {{
            setSuperclass(TARGET.getClass());
            setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    System.out.println("开幕");
//                    System.out.println(o);//cglib生成的代理对象//不能输出
//                    System.out.println("method: " + method);//原方法
//                    System.out.println("objects: " + Arrays.toString(objects));//原方法入参
//                    System.out.println("methodProxy: " + methodProxy);//代理后的方法。与原方法功能相同，为原方法在cglib中的实现
                    Object returnValue = methodProxy.invoke(TARGET, objects);//执行原方法
                    System.out.println("闭幕");
                    return returnValue;
                }
            });
        }};
        Singer PROXY = (Singer) en.create();
        //执行代理对象的方法
        PROXY.sing();
    }
}