package com.msj.lock.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolDemo01 {
    public static void main(String[] args) {
        /*Executors  工具类 三大方法
        Executors.newSingleThreadExecutor(); //单个线程
        Executors.newFixedThreadPool(5); //创建一个固定大小的线程池
        Executors.newCachedThreadPool();  //可伸缩的线程池，遇强则强，遇弱则弱
*/
        //学了县城后使用线程池来创建线程:创建5个线程
        ExecutorService threadPool = Executors.newCachedThreadPool();
        //执行线程
        try {
            for(int i=0;i<10;i++){
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName() + " ok");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程
            threadPool.shutdown();
        }
    }
}
