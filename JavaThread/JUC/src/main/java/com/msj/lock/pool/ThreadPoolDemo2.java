package com.msj.lock.pool;

import java.util.concurrent.*;

public class ThreadPoolDemo2 {
    public static void main(String[] args) {
        //下面的阻塞队列使用的链表队列：new LinkedBlockingQueue<>(3)
        //线程工厂使用默认的线程工程：Executors工具类中有,拒绝策略也可去工具类中找默认值
        //自定义线程池
        ExecutorService threadPool = new ThreadPoolExecutor(2,5,3,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(),new ThreadPoolExecutor.CallerRunsPolicy());

        //执行线程
        try {
            for(int i=0;i<9;i++){
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
