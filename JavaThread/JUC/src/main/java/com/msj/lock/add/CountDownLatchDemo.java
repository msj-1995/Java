package com.msj.lock.add;

import java.util.concurrent.CountDownLatch;

//计数器：CountDown 减法
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        //总数是6，有必须要执行的人数的时候，在使用
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for(int i=0;i<=6;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + " 走了");
                //数量-1
                countDownLatch.countDown(); //-1
            },String.valueOf(i)).start();
        }
        countDownLatch.await();//等待计数器归零，然后在向下执行
        System.out.println("关门");
    }
}
