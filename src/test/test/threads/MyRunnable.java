package threads;

import java.util.concurrent.atomic.AtomicInteger;

class MyRunnable implements Runnable { // 这是一个多线程的操作类
    private ThreadLocal<Integer> tl = ThreadLocal.withInitial(() -> 10);//使多线程各自引用与自己绑定的变量。如果是非引用变量则拷贝，如果是引用变量则为引用（若要实现变量隔离，需使用深拷贝赋值）。
    private AtomicInteger a = new AtomicInteger(10);//AtomicXxx相当于对基础数据类型的原子操作封装类，增加了多种原子操作方法。注意需使用api文档说明中为原子操作的方法，自行组合的方法不能保证原子操作。
    private volatile int b = 10;//https://blog.csdn.net/u011277123/article/details/78180048

    public void run() {
        while (tl.get() > 0) {
            try {
                Thread.sleep(110);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : " + tl.get());
            tl.set(tl.get() - 1);
        }
    }
}
