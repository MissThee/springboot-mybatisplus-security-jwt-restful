package com.example.demo1ioc;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class HelloWorld implements InitializingBean, DisposableBean {
    private String initMethod() {
        System.out.println("initMethod done: " + this.message);
        return "initMethod done";
    }

    private void destroyMethod() {
        System.out.println("destroyMethod done: " + this.message);
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
        System.out.println("initMethod done by InitializingBean: " + this.message);//继承InitializingBean，与xml中配置init-method效果相同。先执行
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("initMethod done by DisposableBean: " + this.message); //继承DisposableBean，与xml中配置destroy-method效果相同。先执行
    }
}