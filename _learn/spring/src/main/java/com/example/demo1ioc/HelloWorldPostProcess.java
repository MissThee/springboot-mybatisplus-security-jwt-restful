package com.example.demo1ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

//bean后置处理器
public class HelloWorldPostProcess implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.printf("%-35s - [%-20s] - %s\n", "postProcessBeforeInitialization() ", "BeanPostProcessor", beanName);
        return bean;  //也可以返回任意对象
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.printf("%-35s - [%-20s] - %s\n", "postProcessAfterInitialization() ", "BeanPostProcessor", beanName);
        return bean;  //也可以返回任意对象
    }
}