package com.msj.gaoji;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

//回顾总结线程的创建
public class ThreadNew {
    public static void main(String[] args) {
        //方式一启动
        new MyThread1().start();

        //方式二启动
        new Thread(new MyThread2()).start();

        //方式三启动:启动方式非常多，这里使用FutureTask,可以加上泛型约束（不加也可以）
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new MyThread3());

        //然后启动，因为Callable本质还是实现了Runnable接口
        new Thread(futureTask).start();

        // FutureTask<Integer> 可以获得返回值
        try {
            Integer integer = futureTask.get();
            System.out.println(integer);  //返回我们在方法中返回的100
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}

//1.继承Thread类
class MyThread1 extends Thread{
    @Override
    public void run() {
        System.out.println("MyThread1");
    }
}

//2.实现Runnable接口
class MyThread2 implements Runnable{
    @Override
    public void run() {
        System.out.println("MyThread2");
    }
}

//3.实现Callable接口
class MyThread3 implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        System.out.println("MyThread3");
        return 100;
    }
}