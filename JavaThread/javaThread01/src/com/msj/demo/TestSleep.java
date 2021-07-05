package com.msj.demo;

//网络模拟延时
public class TestSleep implements Runnable{
    //票数
    private int ticketNum = 10;
    @Override
    public void run() {
        while(true){
            if(ticketNum<=0){
                break;
            }
            //模拟延时
            try{
                Thread.sleep(200);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "-->拿到了第"+ ticketNum-- + "票");
        }
    }

    public static void main(String[] args) {
        TestSleep testSleep = new TestSleep();
        new Thread(testSleep,"小马").start();
        new Thread(testSleep,"小柳").start();
        new Thread(testSleep,"小二").start();
    }
}
