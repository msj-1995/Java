package com.msj.lock.add;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        /*
        * 集齐7颗龙珠召唤神龙
        * */

        //召唤龙珠的线程 参数一：计数值 参数二：Runnable参数，可以使用Lambda简化
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{
            System.out.println("召唤神龙成功");
        });

        for(int i=0;i<7;i++){
            //lambda能拿到操作变量i吗：不能拿到，因为lambda是重新new了一个对象，可以使用中间变量来操作
            final int temp = i;
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "收集了 " + temp + " 颗龙珠");
                try {
                    cyclicBarrier.await();//等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
