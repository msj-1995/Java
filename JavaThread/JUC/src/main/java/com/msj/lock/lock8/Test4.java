package com.msj.lock.lock8;

import java.util.concurrent.TimeUnit;

/*
 *5、增加两个静态的同步方法
 * */
public class Test4 {

    public static void main(String[] args) {
        Phone4 phone = new Phone4();
        Phone4 phone4 = new Phone4();

        new Thread(()->{
            phone.sendSms();
        },"A").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone4.call();
        },"B").start();
    }
}

class Phone4{
    //静态同步方法
    public static synchronized void sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发消息");
    }
    //普通同步方法
    public synchronized void call(){
        System.out.println("打电话");
    }
}
