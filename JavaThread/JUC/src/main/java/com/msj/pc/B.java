package com.msj.pc;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 线程之间的通信问题：生产者消费者问题! 等待唤醒，通知唤醒
 * 线程之间交替执行 A B 操作统一个变量，num=0
 * A num+1
 * B num-1
 * */
public class B {
    public static void main(String[] args) {
        Data2 data = new Data2();

        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"C").start();
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"D").start();
    }
}

//数字 资源类
class Data2{
    private int number = 0;

    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    /*
    * condition.await(); 等待
    * condition.signalAll(); 唤醒全部
    * */

    //+1
    public void increment() throws InterruptedException {
        lock.lock();
        try{
            //等待
            while(number!=0){
                condition.await();
            }

            number++;
            System.out.println(Thread.currentThread().getName() + "--> " + number);
            //通知其他线程，我+1完了(唤醒）
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    //-1
    public void decrement() throws InterruptedException {
        lock.lock();
        try{
            //等待
            while(number==0){
                condition.await();
            }
            number--;
            System.out.println(Thread.currentThread().getName() + "--> " + number);
            //通知其他线程，我-1完毕了（唤醒）
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
