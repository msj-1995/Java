package com.msj.syn;

//安全的买票
public class UnsafeBuyTicket {
    public static void main(String[] args) {
        BuyTicket buyTicket = new BuyTicket();

        new Thread(buyTicket,"苦逼的我").start();
        new Thread(buyTicket,"牛逼的你们").start();
        new Thread(buyTicket,"可恶的黄牛党").start();
    }
}

class BuyTicket implements Runnable{
    private int ticketNums = 10;
    boolean flag = true; //外部停止线程方式
    @Override
    public void run() {
        //买票
        while(flag){
            try {
                buy();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    //同步方法保证安全,它锁的是这个对象：this
    private synchronized void buy() throws InterruptedException {
        if(ticketNums<=0){
            flag = false;
            return;
        }
        //模拟延时
        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getName() + "拿到第 " + ticketNums--);
    }
}
