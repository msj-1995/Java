package com.msj.gaoji;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//测试线程池
public class TestPool {
    public static void main(String[] args) {
        //1、创建线程池 newFixedThreadPool:参数为线程池的大小
        ExecutorService service = Executors.newFixedThreadPool(10);

        //2、向线程池中放入对象启动
        service.execute(new MyThread());
        service.execute(new MyThread());
        service.execute(new MyThread());
        service.execute(new MyThread());

        //3、关闭连接
        service.shutdown();

    }
}

class MyThread implements Runnable{
    @Override
    public void run() {
            System.out.println(Thread.currentThread().getName());
    }
}
