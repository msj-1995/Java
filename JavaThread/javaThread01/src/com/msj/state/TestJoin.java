package com.msj.state;

//测试Join方法，可以想象为插队
public class TestJoin implements Runnable{
    @Override
    public void run() {
        for(int i=0;i<1000;i++){
            System.out.println("线程vip来了" + i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //启动我们的线程
        TestJoin testJoin = new TestJoin();
        Thread thread = new Thread(testJoin);
        thread.start();

        //主线程
        for(int i=0;i<100;i++){
            //主线程跑到200的时候，我们的线程插队,插队后，主线程什么也不能干(其他线程也要等着）
            if(i==10){
                thread.join();
            }
            System.out.println("main " + i);
        }
    }
}
