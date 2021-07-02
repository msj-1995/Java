package com.msj.pc;


/*
* 线程之间的通信问题：生产者消费者问题! 等待唤醒，通知唤醒
* 线程之间交替执行 A B 操作统一个变量，num=0
* A num+1
* B num-1
* */
public class A {
    public static void main(String[] args) {
        Data data = new Data();

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
class Data{
    private int number = 0;

    //+1
    public synchronized void increment() throws InterruptedException {
        //等待
        while(number!=0){
            this.wait();
        }

        number++;
        System.out.println(Thread.currentThread().getName() + "--> " + number);
        //通知其他线程，我+1完了(唤醒）
        this.notify();
    }

    //-1
    public synchronized void decrement() throws InterruptedException {
        //等待
        while(number==0){
            this.wait();
        }
        number--;
        System.out.println(Thread.currentThread().getName() + "--> " + number);
        //通知其他线程，我-1完毕了（唤醒）
        this.notify();
    }
}
