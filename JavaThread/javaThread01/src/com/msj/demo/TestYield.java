package com.msj.demo;

//测试线程礼让，礼让不一定成功，看cup心情
public class TestYield {
    public static void main(String[] args) {
        MyYield myYield = new MyYield();
        new Thread(myYield,"A").start();
        new Thread(myYield,"B").start();
        new Thread(myYield,"C").start();
    }
}

class MyYield implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 线程开始执行");
        Thread.yield();
        System.out.println(Thread.currentThread().getName() + "停止执行");
    }
}