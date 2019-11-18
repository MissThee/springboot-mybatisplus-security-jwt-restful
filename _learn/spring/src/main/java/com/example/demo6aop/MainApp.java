package com.example.demo6aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) throws InterruptedException {
        //需引入依赖
        //<groupId>org.aspectj</groupId>
        //<artifactId>aspectjweaver</artifactId>
        ApplicationContext context = new ClassPathXmlApplicationContext("/bean/demo6aop/Beans.xml");
        Student student = (Student) context.getBean("student");
        student.getName();
        System.out.println("---------------------");
        try {
            student.printThrowException();
        } catch (Exception ignored) {
        }
        System.out.println("---------------------");
        Teacher teacher = (Teacher) context.getBean("teacher");
        teacher.getName();
        System.out.println("---------------------");
        try {
            teacher.printThrowException();
        } catch (Exception ignored) {
        }
        System.out.println("---------------------");
    }
}
