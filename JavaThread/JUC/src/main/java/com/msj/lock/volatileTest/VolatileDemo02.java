package com.msj.lock.volatileTest;

import java.util.concurrent.atomic.AtomicInteger;

//不保证原子性
public class VolatileDemo02 {
    //原子类的Integer
    private volatile static AtomicInteger num = new AtomicInteger();

    public static void add(){
        //num加1操作 原子类的加1操作 使用的是CAS，是cpu的并发操作，效率非常的高
        num.getAndIncrement();
    }

    public static void main(String[] args) {
        //理论上：num的值为1000*20=2万
        for(int i=1;i<=20;i++){
            new Thread(()->{
                for(int j=0;j<1000;j++){
                    add();
                }
            }).start();
        }
        //判断还有多少线程在执行：Java中有两条默认的线程：main和gc线程，如果存活的线程数>2证明还有线程在执行
        while(Thread.activeCount()>2){
            //礼让，如果线程没执行完，我们就让他礼让一下
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + " " + num);
    }
}
