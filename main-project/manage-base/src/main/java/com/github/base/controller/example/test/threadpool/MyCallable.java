package com.github.base.controller.example.test.threadpool;

import java.util.concurrent.Callable;

class MyCallable implements Callable<String> {
    private int index;

    MyCallable(int index) {
        this.index = index;
    }

    @Override
    public String call() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " : " + index);
        return "FINISH: " + index;
    }
}
