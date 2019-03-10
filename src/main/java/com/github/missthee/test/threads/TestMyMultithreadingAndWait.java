package com.github.missthee.test.threads;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class TestMyMultithreadingAndWait {
    private static final int THREAD_NUMBER = 5;

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        testMyRunnable1();
    }

    //thread.join做阻塞同步示例
    private static void testMyRunnable() throws InterruptedException {
        Thread[] threads = new Thread[THREAD_NUMBER];
        for (int i = 0; i < THREAD_NUMBER; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                System.out.println(index);
            }, "R" + i);
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
            System.out.println(thread.getName() + " end");
        }
        System.out.println("ALL FINISHED!!!");
    }

    //countDownLatch.await做阻塞同步示例
    private static void testMyRunnable1() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUMBER);//设置计数初始值
        Thread[] threads = new Thread[THREAD_NUMBER];
        for (int i = 0; i < THREAD_NUMBER; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                System.out.println("before:" + index);
                countDownLatch.countDown();//计数减1（此处并不阻塞）
                System.out.println("after:" + index);//放在计数器操作之后的方法不能保证与后续方法同步，计数为0后，方法会与后续方法并发执行！！！
            }, "R" + i);
            threads[i].start();
        }
        countDownLatch.await();//阻塞，直到计数为0时，唤醒
        System.out.println("ALL FINISHED!!!");
    }

    //countDownLatch.await做阻塞同步示例
    private static void testMyRunnable2() throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_NUMBER, new Runnable() {
            @Override
            public void run() {
                System.out.println("All FINISHED!!!");
            }
        });//设置计数初始目标值
        Thread[] threads = new Thread[THREAD_NUMBER];
        for (int i = 0; i < THREAD_NUMBER; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                System.out.println("before:" + index);
                try {
                    cyclicBarrier.await();//阻塞，计数加1,当加到设置目标值时，执行回调函数，唤醒所有线程
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("after:" + index);
            }, "R" + i);
            threads[i].start();
        }
    }
}
