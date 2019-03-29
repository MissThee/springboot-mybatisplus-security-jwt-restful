package threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

//线程池、Future做阻塞同步示例
public class ThreadPoolAndResultTest {

    private static ExecutorService executorService;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println( Runtime.getRuntime().availableProcessors());

//        List<Future<String>> futureList = cacheThreadPool();
        List<Future<String>> futureList = fixedThreadPool();
//        List<Future<String>> futureList = singleThreadExecutor();
//        List<Future<String>> futureList = scheduledThreadPool();

        for (Future<String> future : futureList) {
            System.out.println(future.get());
        }
        System.out.println("ALL FINISHED!!!");
        //将线程池状态置为SHUTDOWN,并不会立即停止：
        //停止接收外部submit的任务
        //内部正在跑的任务和队列里等待的任务，会执行完
        //等到第二步完成后，才真正停止
        executorService.shutdown();
        //当前线程阻塞，直到
        //等所有已提交的任务（包括正在跑的和队列中等待的）执行完
        //或者等超时时间到
        //或者线程被中断，抛出InterruptedException
        //然后返回true（shutdown请求后所有任务执行完毕）或false（已超时）
        executorService.awaitTermination(0, TimeUnit.MILLISECONDS);
        //将线程池状态置为STOP。企图立即停止，事实上不一定：
        //跟shutdown()一样，先停止接收外部提交的任务
        //忽略队列里等待的任务
        //尝试将正在跑的任务interrupt中断
        //返回未执行的任务列表
        executorService.shutdownNow();
        //优雅的关闭，用shutdown()
        //想立马关闭，并得到未执行任务列表，用shutdownNow()
        //优雅的关闭，并允许关闭声明后新任务能提交，用awaitTermination()
        //关闭功能 【从强到弱】 依次是：shuntdownNow() > shutdown() > awaitTermination()
    }

    //创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
    private static List<Future<String>> cacheThreadPool() {
        List<Future<String>> futureList = new ArrayList<>();
        executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            futureList.add(executorService.submit(new Task(i)));
        }
        return futureList;
    }

    //创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
    private static List<Future<String>> fixedThreadPool() {
        List<Future<String>> futureList = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            futureList.add(executorService.submit(new Task(i)));
        }
        return futureList;
    }

    //创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
    private static List<Future<String>> singleThreadExecutor() {
        List<Future<String>> futureList = new ArrayList<>();
        executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            futureList.add(executorService.submit(new Task(i)));
        }
        return futureList;
    }


    //创建一个定长线程池，支持定时及周期性任务执行。
    private static void scheduledThreadPool() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.schedule(() -> System.out.println("delay 3 seconds"), 3, TimeUnit.SECONDS);//延迟3秒执行
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

