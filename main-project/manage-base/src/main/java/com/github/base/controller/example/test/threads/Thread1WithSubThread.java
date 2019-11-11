package com.github.base.controller.example.test.threads;

public class Thread1WithSubThread {
    private ThreadLocal<String> isAwaitTL = ThreadLocal.withInitial(() -> "aaa");
    private InheritableThreadLocal<String> isAwaitThreadITL = new InheritableThreadLocal<>();

    public static void main(String[] args) {

    }
}
