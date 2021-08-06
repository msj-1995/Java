package com.msj.lock.bq;

import com.msj.pc.A;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        test4();
    }

    /*
     * 抛出异常
     * */
    public static void test1(){
        //参数为队列的大小
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);
        //添加元素，有返回值（布尔值，添加成功返回true，失败返回false)
        System.out.println(blockingQueue.add("A"));
        System.out.println(blockingQueue.add("B"));
        System.out.println(blockingQueue.add("C"));
        //再添加一个元素:抛出异常：队列已满 Queue full
        //System.out.println(blockingQueue.add("D"));

        //弹出元素
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());

        //元素弹完后再出队列 抛出异常：java.util.NoSuchElementException 没有元素
        System.out.println(blockingQueue.remove());
    }

    public static void test2(){
        /*
        * 不抛出异常
        * */
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(3);
        System.out.println(blockingQueue.offer("A"));
        System.out.println(blockingQueue.offer("B"));
        System.out.println(blockingQueue.offer("C"));
        //不抛出异常，队列满了再添加会返回false
        System.out.println(blockingQueue.offer("D"));

        //查看队首元素是谁
        System.out.println(blockingQueue.peek());
        //弹出来
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.peek());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        //对立空继续弹出不跑出异常，而是返回null
        System.out.println(blockingQueue.poll());
    }

    public static void test3() throws InterruptedException {
        /*
        *等待阻塞：一直阻塞
        * */
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(3);

        blockingQueue.put("A");
        blockingQueue.put("B");
        blockingQueue.put("C");
        //队列满，会一直等待
        //blockingQueue.put("D");

        //取
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());  //没有这个元素，也会一直阻塞
    }

    public static void test4() throws InterruptedException {
        /*
        * 阻塞等待，超时退出
        * */
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(3);
        blockingQueue.offer("A");
        blockingQueue.offer("B");
        blockingQueue.offer("C");
        //超时2s后退出
        blockingQueue.offer("D",2, TimeUnit.SECONDS);

        //取
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        //等待2秒拿不到数据就不拿了
        System.out.println(blockingQueue.poll(2,TimeUnit.SECONDS));


    }
}
