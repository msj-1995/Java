package com.msj.lock.unsafe;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //启动Callable
        MyThread myThread = new MyThread();
        //适配类：FutureTask
        FutureTask futureTask = new FutureTask(myThread);
        //执行
        new Thread(futureTask,"A").start();
        new Thread(futureTask,"B").start();

        //结果放到了FutureTask中，可以通过get方法取到
        Integer integer = (Integer)futureTask.get(); //获取Callable的返回结果
        System.out.println(integer);
    }
}


class MyThread implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        System.out.println("call()");
        return 1024;
    }
}