package com.github.missthee.test.multithreading;


class MyThread extends Thread { // Thread实际为Runnable的一个实现类
    MyThread(String name) {
        super(name);
    }

    private ThreadLocal<Integer> tl = ThreadLocal.withInitial(() -> 10);

    public void run() {
        while (tl.get() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : " + tl.get());
            tl.set(tl.get() - 1);
        }
    }
}
