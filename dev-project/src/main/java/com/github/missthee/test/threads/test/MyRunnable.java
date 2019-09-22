package com.github.missthee.test.threads.test;

import java.util.List;
import java.util.concurrent.Callable;

public class MyRunnable implements Runnable {
    private List<ThreadLocal<Integer>> list;

    MyRunnable(List<ThreadLocal<Integer>> list) {
        this.list = list;
    }

    public  ThreadLocal<Integer> tl = new ThreadLocal<Integer>() {
        @Override
        public Integer initialValue() {
            return 10;
        }
    };

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.list.add(tl);
    }
}