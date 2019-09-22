package com.github.missthee.test.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//等待所有线程执行完毕再继续示例
public class Thread1WithLock {

    public static void main(String[] args) {
        MyRunnableWithLock runnable = new MyRunnableWithLock();
//        MyRunnableWithSynchronized runnable=new MyRunnableWithSynchronized();
        runnable.setIsAwaitThread(true);
        new Thread(runnable, "W").start();



        runnable.setIsAwaitThread(false);
        new Thread(runnable, "S").start();
    }

    public static class MyRunnableWithLock implements Runnable {
        //【同一对象在多个线程中（即用一个new MyRunnableWithLock()创建的对象，去创建的多个线程中），普通成员变量（即未使用ThreadLocal包装变量）在这些线程中共享】
        private Lock lock = new ReentrantLock(false);//参数不传或者false都是为非公平锁,为true时表示公平锁。
        //private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();//读写锁特点：多个读可以同时进行读，写与其他读写互斥，写优先于读；rwl.readLock().lock(); rwl.readLock().unlock(); rwl.writeLock().lock(); rwl.writeLock().unlock();
        private Condition condition = lock.newCondition();
//        private static InheritableThreadLocal<Boolean> isAwaitThread = new InheritableThreadLocal<>();//【对应下面】
        private ThreadLocal<Boolean> isAwaitThread = new InheritableThreadLocal<>();
        //InheritableThreadLocal可让父线程为子线程变量赋值
        //源码简要理解：
        //1、调用InheritableThreadLocal的set()方法会获取当前线程(父)Thead对象，此对象中有两成员变量inheritableThreadLocals和threadLocals。InheritableThreadLocal继承于ThreadLocal，重写了部分方法，将值存储到inheritableThreadLocals中
        //2、主线程中创建子线程Thread对象时，Thread的init()方法会判断主线程中的inheritableThreadLocals是否为null，若不为null，将数据深拷贝到新建的子线程Thread对象的inheritableThreadLocals。实现父子线程传递值
        //（实现具体分析可见 https://blog.csdn.net/hewenbo111/article/details/80487252）

        //若InheritableThreadLocal与线程池搭配使用，因线程池中线程可能被缓存起来，重复使用，此时不会对此线程绑定的变量进行初始化操作。
        //则 赋值，执行线程，再赋值，再执行线程，这个过程，可能第二次执行线程时使用的仍是第一次的赋值。
        //解决方案可使用第三方类库 https://github.com/alibaba/transmittable-thread-local
        public void setIsAwaitThread(Boolean isAwaitThread) {
//            MyRunnableWithLock.isAwaitThread.set(isAwaitThread);//【对应上面】//若使用ThreadLocal则此处赋值绑定在主线程，子线程获取时报空指针异常，读取不到值。
            this.isAwaitThread.set(isAwaitThread);

        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " 开始");
            try {
                lock.lock();
//            lock.tryLock(1, TimeUnit.SECONDS);//未超时获取到锁返回true；获取不到则等待，直到超时返回false；未超时被中断了抛出InterruptedException异常。
//            lock.tryLock();//获取到锁返回true，反之false；
//            lock.lockInterruptibly();//lockInterruptibly 一直等待获取锁，等待时被执行interrupt()，立即中断，抛出InterruptedException。 lock方法则是一直等待获取锁，等待时被执行interrupt()，也在获取到锁后，再把当前线程置为interrupted状态,然后再中断线程。
                if (isAwaitThread.get()) {
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
                System.out.println(Thread.currentThread().getName() + " " + isAwaitThread.get());
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
