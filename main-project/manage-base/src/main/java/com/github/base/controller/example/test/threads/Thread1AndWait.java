package com.github.base.controller.example.test.threads;

import java.time.LocalDateTime;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;

//等待所有线程执行完毕再继续示例
public class Thread1AndWait {
    private static final int THREAD_NUMBER = 5;

    public static void main(String[] args) throws InterruptedException {
//        testThread1();
        testRunnableAndWaitAndNoti();
    }

    //thread.join做阻塞同步示例
    private static void testThread() throws InterruptedException {
        Thread[] threads = new Thread[THREAD_NUMBER];
        for (int i = 0; i < THREAD_NUMBER; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(index);
            }, "R" + i);
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();//阻塞主线程，等这一个子线程完成（按顺序等待所有子线程执行完）
            System.out.println(thread.getName() + " end");
        }
        //所有子线程执行完后：
        System.out.println("ALL FINISHED!!!");
    }

    //countDownLatch.await做阻塞同步示例
    private static void testThread1() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUMBER);//设置计数初始值
        Thread[] threads = new Thread[THREAD_NUMBER];
        for (int i = 0; i < THREAD_NUMBER; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                System.out.println("before:" + index);
                countDownLatch.countDown();//计数减1（此处并不阻塞）
                try {
                    countDownLatch.await();//阻塞当前线程（若写在主线程内则会阻塞主线程，），计数为0时：唤醒所有线程，继续执行
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("after:" + index);
            }, "R" + i);
//            countDownLatch.await();//阻塞当前主线程（若写在主线程内则会阻塞主线程，等待子线程完成），计数为0时：唤醒当前主线程，继续执行
            threads[i].start();
        }
        System.out.println("LAST LINE---可能出现在任意行");
    }

    // cyclicBarrier.await做阻塞同步示例
    private static void testThread2() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_NUMBER, new Runnable() {
            //可以设置一个回调函数
            @Override
            public void run() {
                System.out.println("All FINISHED!!!----所有befor和after中间");
            }
        });//设置计数初始目标值
        Thread[] threads = new Thread[THREAD_NUMBER];
        for (int i = 0; i < THREAD_NUMBER; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                System.out.println("before:" + index);
                try {
                    cyclicBarrier.await();//阻塞当前线程，计数加1,当加到设置目标值时：执行回调函数，唤醒所有线程，继续执行
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("after:" + index);
            }, "R" + i);
            threads[i].start();
        }
        System.out.println("LAST LINE---可能出现在任意行");
    }
    //thread WaitAndNoti
    private static void testRunnableAndWaitAndNoti() throws InterruptedException {
      Object o=new Object();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //while循环在try里。
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        o.wait();
                        System.out.println(LocalDateTime.now());
                    }
                } catch (InterruptedException e) {
                    System.out.println("thread interrupted");
                } finally {
                    System.out.println("thread done");
                }
            }
        };

        Thread thread = new Thread(runnable, "R");
        thread.start();
        Thread.sleep(2000);
        o.notify();

    }

}
