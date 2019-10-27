package com.github.base.controller.example.test.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolExecutor1 {
    private static ExecutorService executorService = new ThreadPoolExecutor(
            1,
            2,
            100,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(5)
    );

//核心数corePoolSize、最大数maximumPoolSize、队列BlockingQueue类型及其大小 在线程池中的影响：
//1 如果 运行的线程数 <  corePoolSize，则 Executor 始终首选添加新的线程，而不进行排队。
//2 如果 运行的线程数 >= corePoolSize，则 Executor 始终首选将请求加入队列，而不添加新的线程。
//3 如果无法将请求加入队列，则创建新的线程，除非创建此线程超出 maximumPoolSize，在这种情况下，任务将被拒绝。
//（其中队列长度不能设置为0，可用SynchronousQueue特殊队列构建0队列）

//排队有三种通用策略：
//直接提交。如 SynchronousQueue，它将任务直接提交给线程而不保存。如果不存在可用于立即运行任务的线程，则试图把任务加入队列将直接失败，因此会构造一个新的线程（即上面的规则中队列为0时的流程）。此策略可以避免在处理可能具有内部依赖性的请求集时出现锁。直接提交通常要求无界 maximumPoolSizes 以避免拒绝新提交的任务。
//无界队列。如未定义容量的 LinkedBlockingQueue（其最大容量为无界，此时获取其剩余容量，总返回Integer.MAX_VALUE，但一般使用到不了这个值，认为无界）。 将导致在所有 corePoolSize 线程都忙时，新任务在队列中等待，故创建的线程不会超过 corePoolSize。（因此，maximumPoolSize 的值也就无效了。）当每个任务完全独立于其他任务，即任务执行互不影响时，适合于使用无界队列；例如，在 Web 页服务器中。这种排队可用于处理瞬态突发请求。
//有界队列。如定义容量的 LinkedBlockingQueue，有助于防止资源耗尽，但是可能较难调整和控制。队列大小和最大池大小可能需要相互折衷：1、大型队列和小型池，CPU使用率低(线程少，相对执行慢)，操作系统资源和上下文切换开销小(相对耗时少)。2、小型队和大型池，CPU使用率较高(线程多，相对执行快)，操作系统资源和上下文切换开销大(相对耗时多)。若队列和池都满则进入RejectedExecutionHandler

//RejectedExecutionHandler 的四种饱和策略：
//1	AbortPolicy	        抛出java.util.concurrent.RejectedExecutionException异常，调用的线程可直接捕获
//2	DiscardPolicy	    抛弃当前的任务
//3	DiscardOldestPolicy	抛弃旧的任务，队列头的任务。会抛弃下一个将要执行的任务，如果此策略配合优先队列PriorityBlockingQueue，该策略将会抛弃优先级最高的任务，不好控制。
//4	CallerRunsPolicy	重试添加当前的任务，他会自动重复调用execute()方法。当你的线程数达到最大，阻塞队列也满了的时候，之后的任务会强制先执行，但是没有了线程谁来执行呢，这个策略会强制中断主线程进行执行这个任务（即打印时多出来的一个main线程）。

    //为什么不建议使用Executors中的FixedThreadPool，SingleThreadPool；CachedThreadPool，ScheduledThreadPool四种线程池：
//1	FixedThreadPool,SingleThreadPool	    线程数固定，允许的请求队列长度为无界                  会堆积大量的请求，从而导致 OOM
//3	CachedThreadPool,ScheduledThreadPool 	队列长度固定，允许的创建线程数量为Integer.MAX_VALUE   会创建大量的线程，从而导致 OOM
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        runTest();
//        callTest();
        scheduledThreadPoolTest();
    }

    private static void runTest() throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("线程" + Thread.currentThread());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < 15; i++) {
            executorService.submit(runnable);
        }
        //将线程池状态置为SHUTDOWN，并不会立即停止：
        //1.停止接收外部submit的任务
        //2.内部正在跑的任务和队列里等待的任务，会执行完
        //3.等到第二步完成后，才真正停止
        executorService.shutdown();
        //当前线程阻塞，直到满足其中一条：
        //1.等所有已提交的任务（在跑的和队列中等待的）执行完
        //2.阻塞时间超过了设置的时间
        //3.线程被中断，抛出InterruptedException
        //返回：true- shutdown请求后所有任务执行完毕；false- 超时
        executorService.awaitTermination(0, TimeUnit.MILLISECONDS);
        //将线程池状态置为STOP。企图立即停止，事实上不一定：
        //1.跟shutdown()一样，停止接收外部submit的任务
        //2.忽略队列里等待的任务
        //3.尝试将正在跑的任务interrupt中断
        //4.返回未执行的任务列表
        executorService.shutdownNow();
        //总结：
        //shutdownNow() 立刻关闭，得到未执行任务列表
        //shutdown() 执行完所有已在队列中任务，关闭
        //awaitTermination() 执行完所有已在队列中任务，关闭，并允许关闭声明后新任务能提交
        //关闭功能 【从强到弱】 依次是：shutdownNow() > shutdown() > awaitTermination()
    }

    private static void callTest() throws ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("线程" + Thread.currentThread());
                return "输出：" + Thread.currentThread();
            }
        };
        List<Future<String>> futureList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            futureList.add(executorService.submit(callable));
        }
        for (Future<String> future : futureList) {
            System.out.println(future.get());
        }
        executorService.shutdown();
    }

    //创建一个定长线程池，支持定时及周期性任务执行。
    private static void scheduledThreadPoolTest() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.schedule(() -> {
            System.out.println("delay 3 seconds");
        }, 3, TimeUnit.SECONDS);//延迟3秒后执行一次
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("发起时间计时");
        }, 1, 1, TimeUnit.SECONDS);// 循环任务，表示延迟1秒后执行第次任务，按照上一次任务的发起时间计算下一次任务的开始时间
        scheduledThreadPool.scheduleWithFixedDelay(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("结束时间计时");
        }, 1, 1, TimeUnit.SECONDS);// 循环任务，表示延迟1秒后执行第次任务，以上一次任务的结束时间计算下一次任务的开始时间

    }
}
