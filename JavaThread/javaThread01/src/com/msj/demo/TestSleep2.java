package com.msj.demo;

//模拟倒计时
public class TestSleep2{
    public static void tenDown() throws InterruptedException {
        //倒计初始值
        int num = 10;
        while(true){
            Thread.sleep(1000);
            System.out.println(num--);
            if(num<=0){
                break;
            }
        }
    }

    public static void main(String[] args) {
        try{
            tenDown();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
