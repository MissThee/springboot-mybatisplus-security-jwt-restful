package com.example.demo5event.customevent;

import org.springframework.context.ApplicationListener;

public class CCustomEventHandler  implements ApplicationListener<CustomEvent> {
    public void onApplicationEvent(CustomEvent event) {
        System.out.println(event.toString());
    }
}
