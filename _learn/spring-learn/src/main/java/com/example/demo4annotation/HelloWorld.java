package com.example.demo4annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class HelloWorld {
    @PostConstruct//与@Bean中的initMethod作用相同，先执行
    private String beforeInitializationMethod() {
        System.out.println("beforeInitializationMethod done: " + this.message);
        return "beforeInitializationMethod done";
    }

    @PreDestroy//与@Bean中的destroyMethod作用相同，先执行
    private void afterInitializationMethod() {
        System.out.println("afterInitializationMethod done: " + this.message);
    }

    private String initMethod() {
        System.out.println("initMethod done: " + this.message);
        return "initMethod done";
    }

    private void destroyMethod() {
        System.out.println("destroyMethod done: " + this.message);
    }

    private HelloWorldInner helloWorldInner;

    public HelloWorld(HelloWorldInner helloWorldInner) {
        this.helloWorldInner = helloWorldInner;
    }

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void doSomething() {
        helloWorldInner.doSomething();
    }
}