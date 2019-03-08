package com.github.missthee.test.multithreading;

public class TestMyMultithreadingAndWait { // 主类
    private static final int THREAD_NUMBER = 5;

    public static void main(String[] args) throws InterruptedException {
        testMyRunnable();
    }
//join
    private static void testMyRunnable() throws InterruptedException {
        Thread[] threads = new Thread[THREAD_NUMBER];
        MyRunnable mr = new MyRunnable();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            threads[i] = new Thread(mr, "R" + i);
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
            System.out.println("R" + i + " end\t");
        }
    }

    
}