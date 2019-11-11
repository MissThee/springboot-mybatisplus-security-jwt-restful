package com.github.base.controller.example.test.proxy.proxy1static;

/**
 * 代理对象,静态代理
 */
public class SingerProxy implements ISinger {
    //接收保存目标对象
    private ISinger target;

    public SingerProxy(ISinger target) {
        this.target = target;
    }

    public void sing() {
        System.out.println("开幕");
        target.sing();//执行目标对象的方法
        System.out.println("闭幕");
    }
}