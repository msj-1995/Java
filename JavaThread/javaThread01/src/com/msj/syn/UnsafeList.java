package com.msj.syn;

import java.util.ArrayList;
import java.util.List;

//线程不安全的集合
public class UnsafeList {
    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<String>();
        //开启1000个线程
        for(int i=0;i<10000;i++){
            new Thread(()->{
                //list是变化的量，需要加锁
                synchronized (list){
                    list.add(Thread.currentThread().getName());
                }
            }).start();
        }
        //模拟延时
        Thread.sleep(1000);
        //期待线程的大小为10000
        System.out.println(list.size());
    }
}
