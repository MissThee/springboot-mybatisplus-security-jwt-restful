package com.github.base.controller.example.test.threads.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class sample {
    private static final int THREAD_NUMBER = 4;

    public static void main(String[] args) {
        List<ThreadLocal<Integer>> list = Collections.synchronizedList(new ArrayList<>());
//        MyRunnable mr = new MyRunnable(list);//共用同一MyRunnable对象时，多个线程共享MyRunnable对象中的ThreadLocal对象tl
        Thread[] threads = new Thread[THREAD_NUMBER];
        for (int i = 0; i < THREAD_NUMBER; i++) {
            MyRunnable mr = new MyRunnable(list);//每次创建MyRunnable对象时，多个线程不共享MyRunnable对象中的ThreadLocal对象tl
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
        //如果不使用Collections.synchronizedList包装，list不是线程安全的，此处可能导致list元素还没添加完，就读取了，list.size()输出不为2
        System.out.println("SIZE: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + ". " + (list.get(i) == null ? "null TL" : list.get(i).get()));
        }
        System.out.println("EXECUTE THERE!!!");
    }
}
