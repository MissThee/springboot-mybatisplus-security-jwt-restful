package com.github.missthee.test.threads;

public class TestMyMultithreading { // 主类
    private static final int THREAD_NUMBER = 5;
    public static void main(String[] args) {
        testMyRunnable();
        System.out.println("ALL FINISHED!!!");
    }

    private static void testMyThread() {
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new MyThread("T" + i).start();
        }
    }

    private static void testMyRunnable() {
        MyRunnable mr = new MyRunnable();

        for (int i = 0; i <THREAD_NUMBER; i++) {
            new Thread(mr, "R" + i).start();
        }
    }
}