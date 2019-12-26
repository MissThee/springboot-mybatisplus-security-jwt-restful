package com.example.demo6aop;

public class Teacher {
    private Integer age;
    private String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        System.out.println("执行getName");
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void printThrowException(){
        System.out.println("Teacher Exception");
        throw new IllegalArgumentException();
    }
}