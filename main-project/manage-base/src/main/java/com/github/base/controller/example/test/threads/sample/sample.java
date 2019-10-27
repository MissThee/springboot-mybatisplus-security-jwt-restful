package com.github.base.controller.example.test.threads.sample;

import java.util.ArrayList;
import java.util.List;

public class sample {
    private static final int THREAD_NUMBER = 2;

    public static void main(String[] args) {
        List<ThreadLocal<Integer>> threadLocalList = new ArrayList<>();
//        MyRunnable mr = new MyRunnable(threadLocalList);//俩个线程使用同一MyRunnable对象时，两个线程共享MyRunnable对象中的ThreadLocal对象
        Thread[] threads = new Thread[THREAD_NUMBER];
        for (int i = 0; i < THREAD_NUMBER; i++) {
            MyRunnable mr = new MyRunnable(threadLocalList);
            threads[i] = new Thread(mr);
            threads[i].start();
            if (i == 0) {
                mr.tl = null;//若ThreadLocal为static类型，则此处设置后，两个线程中的ThreadLocal均会变为null
            }
        }
        for (int i = 0; i < THREAD_NUMBER; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(threadLocalList.get(0));
        System.out.println(threadLocalList.get(1));
        System.out.println(threadLocalList.get(0) == threadLocalList.get(1));
        System.out.println("EXECUTE THERE!!!");
    }
}
