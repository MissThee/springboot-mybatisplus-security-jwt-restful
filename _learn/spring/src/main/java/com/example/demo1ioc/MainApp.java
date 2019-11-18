package com.example.demo1ioc;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        //一、使用ApplicationContext。默认读取到bean定义后，立即初始化bean
        if (true) {
            //1、ClassPathXmlApplicationContext加载classpath中的配置文件
            ApplicationContext context = new ClassPathXmlApplicationContext("bean/demo1ioc/Beans.xml");
            //2、FileSystemXmlApplicationContext加载文件系统的配置文件
            //ApplicationContext context = new FileSystemXmlApplicationContext("H:\\WORKWORK_TEST\\spring\\src\\main\\resources\\bean\\Beans.xml");
            HelloWorld helloWorld = (HelloWorld) context.getBean("HelloWorld1");//通过bean的id获取
            System.out.println(helloWorld.getMessage());

            HelloIndia helloIndia = (HelloIndia) context.getBean("HelloIndia");//通过bean的id获取
            System.out.println(helloIndia.getMessage() + " | " + helloIndia.getMessage1());

            HelloIndia helloIndia1 = (HelloIndia) context.getBean("HelloIndia1");//通过bean的id获取
            System.out.println(helloIndia1.getMessage() + " | " + helloIndia.getMessage1());
            ((AbstractApplicationContext) context).registerShutdownHook();//正常关闭的时候关闭容器。关闭容器的时候，会释放所有容器管理Bean，同时如果容器管理Bean声明了销毁回调方法也会执行，以释放资源。不过kill -9，拔电等不在此讨论范围
        }
        //二、使用BeanFactory。默认读取bean定义不初始化bean，真正获取bean时初始化。比使用ApplicationContext更轻量级。
        else {
            //BeanFactory factory = new XmlBeanFactory(new ClassPathResource("Beans.xml"));
            //Spring3.1以后,上面XmlBeanFactory类被废除，新写法为手动写出其实现，如下
            BeanFactory factory = new DefaultListableBeanFactory();
            BeanDefinitionReader reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) factory);
            reader.loadBeanDefinitions(new ClassPathResource("bean/demo1ioc/Beans.xml"));
            HelloWorld helloWorld = (HelloWorld) factory.getBean("HelloWorld1");//通过bean的id获取
            ((BeanDefinitionRegistry) factory).removeBeanDefinition("HelloWorld1");//通过bean的id移除
            System.out.println(helloWorld.getMessage());
        }
        //阻塞
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}