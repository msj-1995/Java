package com.msj.lock.lock8;

import java.util.concurrent.TimeUnit;

/*
 * 1、标准情况下，两个线程先打印发消息还是打电话？  是先发消息，后打电话
 *2、发短信延迟四秒，先打印那个？ 还是先发消息，后打电话
 * 3、一个线程不在调用synchronized是是先打印谁？
 * 4、两个对象执行不同的同步方法，先打印那个？
 * */
public class Test2 {
    public static void main(String[] args) {
        //两个对象
        Phone2 phone = new Phone2();
        Phone2 phone2 = new Phone2();

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
            phone2.call();
        },"B").start();
    }
}


//资源类
class Phone2{

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

    public void hello(){
        System.out.println("hello");
    }
}