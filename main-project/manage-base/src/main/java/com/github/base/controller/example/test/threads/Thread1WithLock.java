package com.github.base.controller.example.test.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//等待所有线程执行完毕再继续示例
public class Thread1WithLock {

    public static void main(String[] args) {
        MyRunnableWithLock runnable = new MyRunnableWithLock();
//        MyRunnableWithSynchronized runnable=new MyRunnableWithSynchronized();
        runnable.setIsAwait(true);
        new Thread(runnable, "W").start();

        runnable.setIsAwait(false);
        new Thread(runnable, "S").start();
    }

    public static class MyRunnableWithLock implements Runnable {
        //【同一对象在多个线程中（即用同一个new MyRunnableWithLock()对象，去创建的多个线程中），此new MyRunnableWithLock()对象中普通成员变量（即未使用ThreadLocal包装变量）在这些线程中共享】
        private Lock lock = new ReentrantLock(false);//参数不传或者false都是为非公平锁,为true时表示公平锁。
        //private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();//读写锁特点：多个读可以同时进行读，写与其他读写互斥，写优先于读；rwl.readLock().lock(); rwl.readLock().unlock(); rwl.writeLock().lock(); rwl.writeLock().unlock();
        private Condition condition = lock.newCondition();
        private ThreadLocal<Boolean> isAwaitTL = new ThreadLocal<>();

        public void setIsAwait(Boolean isAwait) {
            this.isAwaitTL.set(isAwait);
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " 开始");
            try {
                lock.lock();
//            lock.tryLock(1, TimeUnit.SECONDS);//未超时获取到锁返回true；获取不到则等待，直到超时返回false；未超时被中断了抛出InterruptedException异常。
//            lock.tryLock();//获取到锁立即返回true，否则立即返回false；
//            lock.lockInterruptibly();//lockInterruptibly 一直等待获取锁，等待时被执行interrupt()，立即中断，抛出InterruptedException。 lock方法则是一直等待获取锁，等待时被执行interrupt()，也在获取到锁后，再把当前线程置为interrupted状态,然后再中断线程。
                if (isAwaitTL.get()) {
                    //Condition要和Lock联合起来一起用。
                    //调用Condition里的方法，需要先获取相应的Lock，否则抛出IllegalMonitorStateException
                    condition.await();//释放相应的锁且阻塞（所以阻塞时lock.lock()可于其他线程获取到锁），等待.signal()方法激活时，尝试获取当前对象的同步锁，获取到后继续执行。
                } else {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    condition.signal();
                }
                System.out.println(Thread.currentThread().getName() + " " + isAwaitTL.get());
            } catch (InterruptedException e) {
                System.out.println("THREAD INTERRUPTED");
            } finally {
                try {
                    lock.unlock();
                } catch (Exception ignored) {
                }
                System.out.println(Thread.currentThread().getName() + " 结束");
            }
        }
    }

    public static class MyRunnableWithSynchronized implements Runnable {
        //【统一对象在多个线程中，普通成员变量共享】
        private final Object o = new Object();
        private static InheritableThreadLocal<Boolean> isAwaitThread = new InheritableThreadLocal<>();

        public void setIsAwaitThread(Boolean isAwaitThread) {
            MyRunnableWithSynchronized.isAwaitThread.set(isAwaitThread);
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " 开始");
            try {
                synchronized (o) {
                    if (isAwaitThread.get()) {
                        //object.wait()要和synchronized(object)联合起来一起用。
                        //调用object里的线程方法，需要先获取相应的对象锁，否则抛出IllegalMonitorStateException
                        o.wait();//wait()会释放对象的同步锁
                    } else {
                        o.notifyAll();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " " + isAwaitThread.get());
            } catch (InterruptedException e) {
                System.out.println("THREAD INTERRUPTED");
            } finally {
                System.out.println(Thread.currentThread().getName() + " 结束");
            }
        }
    }
}
//Jdk1.6之前，ReentrantLock性能优于synchronized，
//不过1.6之后，synchronized做了大量的性能调优，而且synchronized相对程序员来说，简洁熟悉，
//如果不是synchronized无法实现的功能，如轮询锁、超时锁和中断锁等，推荐首先使用synchronized，
//而针对锁的高级功能，再使用ReentrantLock。
