package com.example.demo4annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BeanConfig {
    @Bean()
    @Scope("singleton")
    public HelloWorldInner helloWorldInner() {
        return new HelloWorldInner();
    }

    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    @Scope("singleton")
    public HelloWorld helloWorld(HelloWorldInner helloWorldInner) {
        HelloWorld helloWorld = new HelloWorld(helloWorldInner);
        helloWorld.setMessage("some info");
        return helloWorld;
    }
}