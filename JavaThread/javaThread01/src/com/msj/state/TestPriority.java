package com.msj.state;

public class TestPriority {
    public static void main(String[] args) {
        //主线程的默认优先级
        System.out.println(Thread.currentThread().getName() + " 主线程优先级-->" + Thread.currentThread().getPriority());

        //其他线程的优先级
        MyPriority myPriority = new MyPriority();
        Thread thread1 = new Thread(myPriority);
        Thread thread2 = new Thread(myPriority);
        Thread thread3 = new Thread(myPriority);
        Thread thread4 = new Thread(myPriority);
        Thread thread5 = new Thread(myPriority);
        Thread thread6 = new Thread(myPriority);

        //先设置优先级，在启动
        thread1.start();

        thread2.setPriority(1);
        thread2.start();

        thread3.setPriority(4);
        thread3.start();

        thread4.setPriority(Thread.NORM_PRIORITY); //NORM_PRIORITY ：5
        thread4.start();

        thread5.setPriority(Thread.MAX_PRIORITY); //MAX_PRIORITY:10
        thread5.start();

        //优先级大于10或者小于-1则报错
        /*thread6.setPriority(11);
        thread6.start();*/

    }
}

class MyPriority implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 优先级为-->" + Thread.currentThread().getPriority());
    }
}
