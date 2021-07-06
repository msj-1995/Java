package com.msj.demo02;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SaleTicketDemo02 {
    public static void main(String[] args) {
        Ticket2 ticket2 = new Ticket2();
        /*new Thread(()->{
            for(int i=0;i<40;i++){
                ticket2.sale();
            },
        },"A").start();
        new Thread(()->{
            for(int i=0;i<40;i++){
                ticket2.sale();
            },
        },"B").start();
        new Thread(()->{
            for(int i=0;i<40;i++){
                ticket2.sale();
            },
        },"C").start();*/
        //简化
        new Thread(()->{for(int i=0;i<40;i++) ticket2.sale();},"A").start();
        new Thread(()->{for(int i=0;i<40;i++) ticket2.sale();},"B").start();
        new Thread(()->{for(int i=0;i<40;i++) ticket2.sale();},"C").start();
    }
}

//lock锁 三部曲
//1.定义锁new ReentrantLock();  2.加锁 lock.lock() 3.解锁 lock.unlock
class Ticket2{
    private int number = 30;

    //使用可重入锁
    Lock lock = new ReentrantLock();

    public void sale(){
        lock.lock();;
        try{
            //业务代码
            if(number>0){
                System.out.println(Thread.currentThread().getName() + "卖出了第 " + number-- + "票，剩余 " + number + "票");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            //解锁
            lock.unlock();
        }
    }
}
