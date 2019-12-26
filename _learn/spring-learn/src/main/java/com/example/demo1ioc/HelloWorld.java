package com.example.demo1ioc;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

public class HelloWorld implements InitializingBean, DisposableBean {
    HelloWorld() {
        System.out.println("HelloWorld构造函数执行");
    }

    private String initMethod() {
        System.out.printf("%-35s - [%-20s] - %s\n", "initMethod func()", "init-method xml配置", this.message);
        return "可以返回点儿没什么用的值";
    }

    @PostConstruct
    private void postConstruct() {
        System.out.printf("%-35s - [%-20s] - %s\n", "postConstruct func()", "@PostConstruct注解", this.message);
    }

    private void destroyMethod() {
        System.out.printf("%-35s - [%-20s] - %s\n", "destroyMethod func()", "destroy-method xml配置", this.message);
    }

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.printf("%-35s - [%-20s] - %s\n", "afterPropertiesSet()", "InitializingBean接口", this.message);//继承InitializingBean，与xml中配置init-method效果相同。先执行
    }

    @Override
    public void destroy() throws Exception {
        System.out.printf("%-35s - [%-20s] - %s\n", "destroy()", "DisposableBean接口", this.message); //继承DisposableBean，与xml中配置destroy-method效果相同。先执行
    }
}