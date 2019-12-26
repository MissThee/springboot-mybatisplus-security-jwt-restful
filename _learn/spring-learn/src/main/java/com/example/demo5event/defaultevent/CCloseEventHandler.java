package com.example.demo5event.defaultevent;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;

public class CCloseEventHandler implements ApplicationListener<ContextClosedEvent> {
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("Closed Event Received");
    }
}