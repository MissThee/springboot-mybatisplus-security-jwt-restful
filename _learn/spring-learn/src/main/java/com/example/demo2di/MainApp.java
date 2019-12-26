package com.example.demo2di;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context =new ClassPathXmlApplicationContext("bean/demo2di/Beans.xml");
        TextEditor te = (TextEditor) context.getBean("textEditor");
        te.spellCheck();
        TextEditor te1 = (TextEditor) context.getBean("textEditor1");
        te1.spellCheck();
        JavaCollection jc=(JavaCollection)context.getBean("javaCollection");
        jc.getAddressList();
        jc.getAddressSet();
        jc.getAddressMap();
        jc.getAddressProp();
    }
}
