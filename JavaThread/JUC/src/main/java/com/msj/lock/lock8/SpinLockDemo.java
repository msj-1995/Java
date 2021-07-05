package com.msj.lock.lock8;

import java.util.concurrent.atomic.AtomicReference;

/*
* 自旋锁
* */
public class SpinLockDemo {
    //使用原子引用 泛型：Thread，默认为null
    AtomicReference<Thread> atomicReference = new AtomicReference<>();
    //加锁
    public void myLock(){
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "==>myLock");
        //如果当前线程不空，就让他一直循环 自旋锁
        while(!atomicReference.compareAndSet(null,thread)){}
    }

    //解锁
    public void myUnLock(){
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "==>myUnlock");
        //解锁
        atomicReference.compareAndSet(thread,null);
    }


}
