package com.msj.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

//
public class CASDemo {
    //CAS comparAndSet 比较并交换！
    public static void main(String[] args) {
        //原子类的Integer
        //AtomicInteger atomicInteger = new AtomicInteger(2020);

        //使用原子引用
        //new AtomicReference<>(),基本类，还有一个带时间戳的类，如下:参数一：期望值，参数二：被修改过就+1，相当于版本号
        AtomicStampedReference<Integer> integerAtomicStampedReference = new AtomicStampedReference<>(1,1);

        new Thread(()->{
            int stamp = integerAtomicStampedReference.getStamp();//获得版本号
            System.out.println("a1-->" + stamp);
            //为了保证两个线程不同时执行，该线程延迟一会儿执行
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*
        * public boolean compareAndSet(V   expectedReference,
                                 V   newReference,
                                 int expectedStamp,
                                 int newStamp)
         四个参数：参数三：期望的时间戳  参数四：新的（更新的值）时间戳
        * */
            integerAtomicStampedReference.compareAndSet(1, 2, integerAtomicStampedReference.getStamp(), integerAtomicStampedReference.getStamp() + 1);
            System.out.println("a2-->" + integerAtomicStampedReference.getStamp());

            integerAtomicStampedReference.compareAndSet(2, 1, integerAtomicStampedReference.getStamp(), integerAtomicStampedReference.getStamp() + 1);
            System.out.println("a3--> " + integerAtomicStampedReference.getStamp());
        },"a").start();

        //希望上面的线程修改过，下面的线程是知道的
        new Thread(()->{
            int stamp = integerAtomicStampedReference.getStamp();
            System.out.println("a2--> " + stamp);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(integerAtomicStampedReference.compareAndSet(1, 6, stamp, stamp + 1));
            System.out.println("b1-->" + integerAtomicStampedReference.getStamp());
        },"b").start();
    }
}
