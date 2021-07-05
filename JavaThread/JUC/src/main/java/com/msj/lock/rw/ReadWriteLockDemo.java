package com.msj.lock.rw;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();
        //写入
        for(int i=1;i<=5;i++){
            final int temp = i;
            new Thread(()->{
                myCache.put(temp+"",temp+"");
            },String.valueOf(i)).start();
        }

        //读取
        for(int i=1;i<=5;i++){
            final int temp = i;
            new Thread(()->{
                myCache.get(temp+"");
            },String.valueOf(i)).start();
        }
    }
}

/*
* 自定义缓存
* */

class MyCache{
    private volatile Map<String,Object> map = new HashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    //存，写
    public void put(String key,Object value){
        //写锁
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " 写入" + key);
            map.put(key,value);
            System.out.println(Thread.currentThread().getName() + "写入完毕" + key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    //读
    public void get(String key){
        //读锁
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "读取" + key);
            Object o = map.get(key);
            System.out.println(Thread.currentThread().getName() + "读取成功" + key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }
}