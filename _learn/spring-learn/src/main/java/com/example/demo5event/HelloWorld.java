package com.example.demo5event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class HelloWorld implements ApplicationListener<ApplicationEvent> {
    private String message;
    public void setMessage(String message){
        this.message  = message;
    }
    public void getMessage(){
        System.out.println("Your Message : " + message);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        //继承ApplicationListener泛型使用ApplicationEvent可监听所有类型事件。
        System.out.println("event: "+event.getClass());
    }
}