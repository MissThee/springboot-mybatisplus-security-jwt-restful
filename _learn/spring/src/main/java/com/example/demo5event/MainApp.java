package com.example.demo5event;

import com.example.demo5event.customevent.CustomEventPublisher;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("bean/demo5event/Beans.xml");
        CustomEventPublisher publisher = (CustomEventPublisher) ctx.getBean("customEventPublisher");
        publisher.publish();//发布自定义事件
//        ctx.refresh();//ctx容器初始化完成后，会触发refresh事件
        System.out.println("ctx.isRunning(): "+ctx.isRunning());//true
        ctx.start();
        ctx.stop();
        System.out.println("ctx.isRunning(): "+ctx.isRunning());//false
//        ctx.close();//注册registerShutdownHook后，ctx关闭时，会触发close事件
        ctx.registerShutdownHook();
    }
    //由于 Spring 的事件处理是单线程的，所以一个事件被发布，直至并且除非所有的接收者得到该消息前，该进程被阻塞并且流程将不会继续。因此，如果事件处理被使用，在设计应用程序时应注意。
}