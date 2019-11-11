package com.github.base.controller.example.test.threads;

//InheritableThreadLocal父子线程传递值
public class Thread1AndInheritableThreadLocal {
    public static void main(String[] args) {
        //主线程中赋值
        ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
        InheritableThreadLocal<String> stringInheritableThreadLocal = new InheritableThreadLocal<>();
        stringThreadLocal.set("ThreadLocal string");
        stringInheritableThreadLocal.set("InheritableThreadLocal string");
        System.out.println(Thread.currentThread().getName() + " MAIN ");
        //子线程中分别打印两个变量的信息
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " TL  value ：" + stringThreadLocal.get());
            System.out.println(Thread.currentThread().getName() + " ITL value ：" + stringInheritableThreadLocal.get());
        });
        thread.start();
        System.out.println("END");
        //InheritableThreadLocal可让父线程为子线程变量赋值。源码简要理解：
        //1、调用InheritableThreadLocal的set()方法会获取当前线程(父)Thead对象，此对象中有两成员变量inheritableThreadLocals和threadLocals。InheritableThreadLocal继承于ThreadLocal，重写了部分方法，将值存储到inheritableThreadLocals中
        //2、主线程中创建子线程Thread对象时，Thread的init()方法会判断主线程中的inheritableThreadLocals是否为null，若不为null，将数据深拷贝到新建的子线程Thread对象的inheritableThreadLocals。实现父子线程传递值
        //（实现具体分析可见： https://blog.csdn.net/hewenbo111/article/details/80487252    ；  https://www.jianshu.com/p/a1d4cce7af53）
        //注：若InheritableThreadLocal与线程池搭配使用，因线程池中线程可能被缓存起来，重复使用同一线程时，不会再对此线程绑定变量进行初始化操作。解决方案可使用第三方类库 https://github.com/alibaba/transmittable-thread-local
    }
}