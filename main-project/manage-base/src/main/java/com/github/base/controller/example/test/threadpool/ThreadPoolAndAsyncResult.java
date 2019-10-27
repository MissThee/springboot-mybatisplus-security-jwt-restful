package com.github.base.controller.example.test.threadpool;

import java.util.concurrent.*;

//线程池、Future做阻塞同步示例
public class ThreadPoolAndAsyncResult {

    private static ExecutorService executorService = Executors.newFixedThreadPool(3);;

    //先完成的先输出结果
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(Runtime.getRuntime().availableProcessors());
        CompletionService<String> completionService = fixedThreadPool();
        for (int i = 0; i < 10; i++) {
//            Future<String> result = completionService.poll();//获取并移除已完成状态的task，如果目前不存在这样的task，直接返回null
             Future<String> result = completionService.take();//阻塞，等待执行完任一task，获取并移除已完成状态的task
            System.out.println(result != null ? result.get() : "NULL TASK");
        }
        System.out.println("ALL FINISHED!!!");
        executorService.shutdown();
    }

    private static CompletionService<String> fixedThreadPool() {
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);
        for (int i = 0; i < 10; i++) {
            completionService.submit(new MyCallable(i));
        }
        return completionService;
    }

}

