package com.msj.lock.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FutureDemo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //有返回值的异步回调：CompletableFuture.supplyAsync()需要传入一个共给型的函数接口
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName() + " supplyAsync-->completableFuture");
            int i=1/0;
            return 1024;
        });

        //completableFuture.whenComplete()编译成功时的回调，传入参数为BiConsumer,增强版的消费型接口
        System.out.println(completableFuture.whenComplete((t, u) -> {
            System.out.println("t-->" + t);
            System.out.println("u-->" + u);
            //exceptionally()编译失败的回调(参数是一个函数型接口） 传的参数的异常类,
        }).exceptionally((e) -> {
            //e.getMessage()获取异常信息
            System.out.println(e.getMessage());
            //打印堆栈信息
            e.printStackTrace();
            return 233;
            //get()：获得结果
        }).get());
        //正常返回1024，失败返回223
    }
}
