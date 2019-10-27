package com.github.base.controller.example.test.reactor;

import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class test1 {
    public static void main(String[] args) {
        displayValue(null, 1);
        //创建一个数据源
        Mono.just(new Random().nextInt(10))
                //延迟几秒再发射数据
                .delayElement(Duration.ofSeconds(3))
                //在数据上执行一个转换
                .map(n -> {
                    displayValue(n, 2);
                    return n + 1;
                })
                //在数据上执行一个过滤
                .filter(n -> {
                    displayValue(n, 3);
                    delaySeconds(3);
                    return n % 2 == 0;
                })
                //如果数据没了就用默认值
                .defaultIfEmpty(100)
                //订阅一个消费者把数据消费了
                .subscribe(n -> {
                    displayValue(n, 4);
                    delaySeconds(2);
                    System.out.println(n + " consumed, worker Thread over, exit.");
                });
        displayValue(null, 5);
        pause();
    }

    //显示当前的数值
    private static void displayValue(Integer n, int point) {
        System.out.println("STEP" + point + " : " + "INPUT : " + n + " [Thread " + Thread.currentThread().getId() + "][Time " + LocalTime.now() + "]");
    }

    //延迟若干秒
    private static void delaySeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //主线程暂停
    private static void pause() {
        try {
            System.out.println("main Thread over, paused.");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
