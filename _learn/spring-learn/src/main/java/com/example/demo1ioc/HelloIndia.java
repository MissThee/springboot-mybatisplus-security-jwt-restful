package com.example.demo1ioc;

public class HelloIndia {
    HelloIndia() {
        System.out.println("HelloIndia构造函数执行");
    }

    private String message;

    private String message1;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage1(String message) {
        this.message1 = message;
    }

    public String getMessage1() {
        return this.message1;
    }

}