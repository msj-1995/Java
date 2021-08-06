package com.msj.gaoji;

import java.util.concurrent.locks.ReentrantLock;

//测试Lock锁
public class TestLock {
    public static void main(String[] args) {
        TestLock2 testLock2 = new TestLock2();
        new Thread(testLock2,"A").start();
        new Thread(testLock2,"B").start();
        new Thread(testLock2,"C").start();
    }
}

class TestLock2 implements Runnable{
    private int ticketNums = 10;

    //定义lock锁
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        while(true){
            //加锁：一般把加锁的操作放在try--catch语句块中,解锁放在finally中
            try{
                //加锁
                lock.lock();
                if(ticketNums>0){
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "获得第" + ticketNums--);
                }
                else{
                    break;
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                //解锁
                lock.unlock();
            }
        }

    }
}
