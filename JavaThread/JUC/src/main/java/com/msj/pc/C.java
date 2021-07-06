package com.msj.pc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
*
* */
public class C{
    public static void main(String[] args) {
        Data3 data = new Data3();
        //目的：希望A执行完通知B执行，B执行完通知C执行，C执行完在通知A执行，如此反复
        new Thread(()->{
            for(int i=0;i<10;i++){
                data.printA();
            }
        },"A").start();
        new Thread(()->{for(int i=0;i<10;i++) data.printB();},"B").start();
        new Thread(()->{for(int i=0;i<10;i++) data.printC();},"C").start();
    }
}


class Data3{
    private Lock lock = new ReentrantLock();
    //Condition 同步监视器，甚至可以一个监视器监视一个线程 condition1 A;condition2:B condition3监视C
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    private int num = 1; //num=1,A执行，2：B执行 3：C执行

    public void printA(){
        lock.lock();
        try {
            //业务 判断等待->业务->执行
            while(num!=1){
                //等待
                condition1.await();
            }
            //业务
            System.out.println(Thread.currentThread().getName() + " --->执行");
            //唤醒 唤醒指定的人 B
            num = 2;
            //唤醒condition2
            condition2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printB(){
        lock.lock();
        try {
            //业务 判断等待->业务->执行
            while(num!=2){
                //等待
                condition2.await();
            }
            //业务
            System.out.println(Thread.currentThread().getName() + " --->执行");
            //唤醒 唤醒指定的人 B
            num = 3;
            //唤醒condition3
            condition3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void printC(){
        lock.lock();
        try {
            //业务 判断等待->业务->执行
            while(num!=3){
                condition3.await();
            }
            num=1;
            System.out.println(Thread.currentThread().getName() + " --->执行");
            //唤醒condition1锁
            condition1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

}