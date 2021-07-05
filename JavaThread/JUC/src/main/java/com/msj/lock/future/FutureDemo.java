package com.msj.lock.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/*
* 异步调用：可以想象为Ajax
*
* 步骤 异步调用：CompletableFuture
* 1、异步执行
* 2、成功回调
* 3、失败回调
* */
public class FutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //发起一个请求 如果没有返回值，泛型写Void(注意类型不能写小写，需要用大写，包装类） CompletableFuture<Void>
        //CompletableFuture.runAsync();使用Runnable方式启动
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "runAsync=>Void");
        });

        //由于是异步执行，在线程休眠的时候应该不影响主线程的执行
        System.out.println("1111111111");
        //获取执行结果：completableFuture.get();会阻塞等待结果
        completableFuture.get();
    }
}
