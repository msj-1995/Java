package com.msj.lock.volatileTest;

import java.util.concurrent.TimeUnit;

public class JMMDemo {
    private volatile static int num = 0;
    public static void main(String[] args){
        //main线程（主线程）

        //在添加一个线程一
        new Thread(()->{
            while(num==0){

            }
        }).start();;

        //主线程休息1s，是为了保证子线程能够成功启动
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        num=1;
        System.out.println(num);
    }
}
