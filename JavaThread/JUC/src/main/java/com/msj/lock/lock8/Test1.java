package com.msj.lock.lock8;

import java.util.concurrent.TimeUnit;

/*
* 1、标准情况下，两个线程先打印发消息还是打电话？  是先发消息，后打电话
*2、发短信延迟四秒，先打印那个？ 还是先发消息，后打电话
* */
public class Test1 {
    public static void main(String[] args) {
        Phone phone = new Phone();

        //两个线程，一个线程打电话，一个线程发消息
        new Thread(()->{
            phone.sendSms();
        },"A").start();
        //发完消息后休息1秒钟
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone.call();
        },"B").start();
    }
}


//资源类
class Phone{

    public synchronized void sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发送消息");
    }

    public synchronized void call(){
        System.out.println("打电话");
    }
}