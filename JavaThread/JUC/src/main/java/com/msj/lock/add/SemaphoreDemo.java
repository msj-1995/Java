package com.msj.lock.add;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    public static void main(String[] args) {
        //参数：线程数量 限流
        Semaphore semaphore = new Semaphore(3);

        //六辆车去抢3个停车位
        for(int i=1;i<=6;i++){
            new Thread(()->{
                //acquire 得到 开启线程后需要先得到一个停车位
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " 抢到了车位");
                    //停一会儿
                    TimeUnit.SECONDS.sleep(2);
                    //离开车位
                    System.out.println(Thread.currentThread().getName() + " 离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    //释放
                    semaphore.release();
                }

                //release() 释放
            },String.valueOf(i)).start();
        }
    }
}
