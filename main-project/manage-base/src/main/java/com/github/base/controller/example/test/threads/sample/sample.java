package com.github.base.controller.example.test.threads.sample;

import java.util.ArrayList;
import java.util.List;

public class sample {
    private static final int THREAD_NUMBER = 2;

    public static void main(String[] args) {
        List<ThreadLocal<Integer>> list = new ArrayList<>();
//        MyRunnable mr = new MyRunnable(threadLocalList);//两个线程使用同一MyRunnable对象时，两个线程共享MyRunnable对象中的ThreadLocal对象tl
        Thread[] threads = new Thread[THREAD_NUMBER];
        for (int i = 0; i < THREAD_NUMBER; i++) {
            MyRunnable mr = new MyRunnable(list);
            threads[i] = new Thread(mr);
            threads[i].start();
            mr.tl.set(i);
        }
        for (int i = 0; i < THREAD_NUMBER; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("0. " + list.size());
        System.out.println("1. " + (list.get(0) == null ? "null TL" : list.get(0).get()));
        System.out.println("2. " + (list.get(1) == null ? "null TL" : list.get(1).get()));
        System.out.println("EXECUTE THERE!!!");
    }
}
