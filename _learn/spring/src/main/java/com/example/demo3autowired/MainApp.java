package com.example.demo3autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean/demo3autowired/Beans.xml");
        TextEditor te = (TextEditor) context.getBean("textEditor");
        te.spellCheck();
        System.out.println(te.getNote());
        TextEditor1 te1 = (TextEditor1) context.getBean("textEditor1");
        te1.spellCheck();
        System.out.println(te1.getNote());
    }
}
