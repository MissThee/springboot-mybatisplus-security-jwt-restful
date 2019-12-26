package com.example.demo7jdbc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class MainApp {
    //jdbc依赖
    //<groupId>org.springframework</groupId>
    //<artifactId>spring-jdbc</artifactId>
    //mysql数据库引擎
    //<groupId>mysql</groupId>
    //<artifactId>mysql-connector-java</artifactId>
    //数据库事务
    //<groupId>org.springframework</groupId>
    //<artifactId>spring-tx</artifactId>
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/bean/demo7jdbc/Beans.xml");
        StudentDAO studentDAO = (StudentDAO) context.getBean("studentDAO");
        System.out.println("------Records Creation--------");
        studentDAO.create("A", "B", "C", "D", "E", "F");
        try {
            studentDAO.create1("1", "2", "3", "4", "5", "6");
        } catch (Exception e) {
            System.out.println(e + "ROLL BACK!!");
        }
        System.out.println("------Listing Multiple Records--------");
        List<Student> students = studentDAO.listStudents();
        for (Student record : students) {
            System.out.println("ID : " + record.getId() + ", Name : " + record.getName());
        }
        System.out.println("----Updating Record with ID = 2 -----");
        studentDAO.update(2, "张三");
        System.out.println("----Listing Record with ID = 2 -----");
        Student student = studentDAO.getStudent(2);
        if (student != null) {
            System.out.println("ID : " + student.getId() + ", Name : " + student.getName());
        } else {
            System.out.println("NO RECORD");
        }
//        studentDAO.deleteAll();
    }
}
