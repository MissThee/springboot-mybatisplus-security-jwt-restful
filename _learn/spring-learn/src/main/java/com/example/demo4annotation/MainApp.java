package com.example.demo4annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        //加载配置类
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
//        {//也可这样加载配置类
//            AnnotationConfigApplicationContext ctx1 = new AnnotationConfigApplicationContext();
//            ctx1.register(HelloWorldConfig.class);
//            ctx1.refresh();
//        }

        HelloWorld helloWorld = ctx.getBean(HelloWorld.class);
        System.out.println(helloWorld.getMessage());
        helloWorld.doSomething();
        ctx.registerShutdownHook();
    }
}
