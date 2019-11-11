package com.github.base.controller.example.test.proxy.proxy1static;

//静态代理。目标对象须实现一个或多个接口。
public class SingerTest {
    public static void main(String[] args) {
        //原对象
        ISinger TARGET = new Singer();
        //代理对象,把目标对象传给代理对象
        ISinger PROXY = new SingerProxy(TARGET);
        //执行的是代理的方法，其中包含了原方法
        PROXY.sing();
    }
}
