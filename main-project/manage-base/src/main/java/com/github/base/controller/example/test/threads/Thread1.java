package com.github.base.controller.example.test.threads;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Thread1 { // 主类
    private static final int THREAD_NUMBER = 3;

    public static void main(String[] args) {
//        testMyRunnable();
        testMyThread();
//        testMyCallable();
//        testRunnableAndInterrupt();
        System.out.println("EXECUTE THERE!!!");
    }

    //1
    private static void testMyRunnable() {
        for (int i = 0; i < THREAD_NUMBER; i++) {
            System.out.println("NEW ONE!!!");
            MyRunnable mr = new MyRunnable();
            Thread thread = new Thread(mr, "R" + i);
            thread.setUncaughtExceptionHandler((t, e) -> {//异常处理器。线程发生异常后，执行此方法
                System.out.println("异常 " + t.getName() + ": " + e.toString());
            });
            thread.start();
        }
    }

    public static class MyRunnable implements Runnable {
        private ThreadLocal<Integer> tl = ThreadLocal.withInitial(() -> 10);

        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (tl.get() > 0) {
                        Thread.sleep(110);
                        System.out.println(Thread.currentThread().getName() + " : " + tl.get());
                        tl.set(tl.get() - 1);
                        if (Thread.currentThread().getName().contains("1")) {
                            tl = null;
                        }
                    } else {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("线程中断");
                //线程在wait或sleep期间被中断了
            } finally {
                //线程结束前做一些清理工作
                if (tl != null) {
                    tl.remove();
                }
            }
        }
    }

    //2
    private static void testMyThread() {
        MyThread thread1 = new MyThread();
        MyThread thread2 = new MyThread();
        thread1.setName("T1");
        thread2.setName("T2");
        thread1.start();
        thread2.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt();
    }

    public static class MyThread extends Thread { // Thread实际为Runnable的一个实现类
        private static ThreadLocal<Integer> tl = ThreadLocal.withInitial(() -> 10);

        public void run() {
            //try在while循环里
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (tl.get() > 0) {
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + " : " + tl.get());
                        tl.set(tl.get() - 1);
                    } else {
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();//设置中断标志为true，使while退出循环
                }
            }
            tl.remove();
        }
    }

    //3
    private static void testMyCallable() {
        {
            for (int i = 0; i < THREAD_NUMBER; i++) {
                System.out.println("NEW ONE!!!");
                MyCallable mc = new MyCallable();
                FutureTask<String> futureTask = new FutureTask<>(mc);
                Thread thread = new Thread(futureTask, "C" + i);

                thread.start();
//                try {
//                    System.out.println(futureTask.get());//get()方法会阻塞。
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    public static class MyCallable implements Callable<String> {
        private ThreadLocal<Integer> tl = new ThreadLocal<Integer>() {
            @Override
            public Integer initialValue() {
                return Double.valueOf(10).intValue();
            }
        };
        //private final ThreadLocal<Integer> tl = ThreadLocal.withInitial(() -> (int) (Math.random() * 10));//withInitial java8提供的新写法

        //Thread中有变量 ThreadLocal.ThreadLocalMap threadLocals = null;（线程初始化即new Thread()时，threadLocals进行初始化，为null）
        //ThreadLocalMap结构：Entry(ThreadLocal<?> k, Object v)
        //tl.get();调用时：若ThreadLocalMap为null(首次访问肯定为null)，则进行初始化new ThreadLocalMap(this, firstValue),其中firstValue值从initialValue()中获取，可重写此接口设置初始值；否则直接从ThreadLocalMap中获取

        //ThreadLocal 加 private static final 修饰理解：（tl指ThreadLocal对象本身，不是它取到的值）
        //static：
        //    使用static时，使用同一Runnable类的对象，初始化的Thread对象，当改变tl时，他们都会受到影响；不使用时，使用同一Runnable对象初始化的Thread对象，当tl改变时，他们都会受到影响
        //    只要明确知道Runnable中的ThreadLocal是线程对象中ThreadLocalMap中的key值，且Thread初始化时使用的Runnable对象为引用不是拷贝，其他按照普通对象的用域理解就可以
        //private：
        //    限制访问范围（tl作为获取本线程隔离参数的key值，不推荐在外部对其进行修改，如运行时设置tl=null则这个线程隔离变量就获取不到了）
        //final：
        //    限制可修改性（tl作为获取本线程隔离参数的key值，可保证其不被再次修改，如运行时设置tl=null则这个线程隔离变量就获取不到了）

        //private AtomicInteger a = new AtomicInteger(10);
        //AtomicInteger注:
        //AtomicXxx相当于对基础数据类型的原子操作封装类，增加了多种原子操作方法。注意需使用api文档说明中为原子操作的方法，自行组合的方法不能保证原子操作。

        //private volatile int b = 10;
        //volatile注：
        //  特殊规则：
        //将读取操作read,load,user，写入操作assign,store,write变成两组必须连续出现的原子操作，修改后的值会立即更新到主存，但不能保证读写内容一致。https://blog.csdn.net/u011277123/article/details/78180048
        // 主要效果：
        //1. 每次写入后必须立即同步回主内存当中。
        //2. 每次读取前必须先从主内存刷新最新的值。（其他处理器若使用此值需重新读取，而不是使用自己工作内存中的缓存）
        // 适用场景：
        //多个变量之间或者某个变量当前值与修改后值之间没有约束时
        //注：不能保证读写一致：即有static静态变量i=0;两个线程做：i=i+1（非原子操作，分读取、计算、赋值步骤），结果可能为1。

        @Override
        public String call() throws Exception {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (tl.get() > 0) {
                        Thread.sleep(100);
                        System.out.println(tl + " " + Thread.currentThread().getName() + " : " + tl.get());
                        tl.set(tl.get() - 1);
                        if (Thread.currentThread().getName().contains("1")) {
                            tl = null;//将线程名字包含1中的tl设置为null，这个线程的变量就获取不到了
                        }
                    } else {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();//重新设置中断标识
                }
            }
            if (tl != null) {
                tl.remove();
            }
            return "Callable END";
        }
    }

    //4    thread中断示例
    private static void testRunnableAndInterrupt() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //while循环在try里。
//                try {
//                    while (!Thread.currentThread().isInterrupted()) {
//                        Thread.sleep(3000);
//                        System.out.println(LocalDateTime.now());
//                    }
//                } catch (InterruptedException e) {
//                    System.out.println("thread interrupted");
//                } finally {
//                    System.out.println("thread done");
//                }
                //try在while循环里。
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(LocalDateTime.now());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();//设置中断标示为true，使while可以退出循环
                        System.out.println("thread interrupted");
                    } finally {
                        System.out.println("thread done");
                    }
                }
            }
        };

        Thread thread = new Thread(runnable, "R");
        thread.start();
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
        //调用interrupt()后，将线程的isInterrupted值变为true
        //1. 若线程在内部方法阻塞中，如thread.sleep、thread.join、thread.wait、1.5中的condition.await,则会立即抛出异常InterruptedException,编写代码catch此异常，跳出循环，结束线程
        //2. 若线程无阻塞方法，则需在循环体判断Thread.currentThread().isInterrupted()的值，跳出循环，结束线程
        //Thread类里的几个方法：
        //public static boolean interrupted()	    获取当前线程中断状态，并清除中断状态（即设置isInterrupted()为false）
        //public boolean isInterrupted()    获取当前线程中断状态。
        //public void interrupt()    设置中断状态为true。
    }


}