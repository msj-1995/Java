package com.msj.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestSleep3 {
    public static void main(String[] args) {
        int num = 10;
        //打印系统当前时间
        Date startTime = new Date(System.currentTimeMillis());//获取系统当前时间
        while(true){
            try{
                Thread.sleep(1000);
                System.out.println(new SimpleDateFormat("HH:mm:ss").format(startTime));
                startTime = new Date(System.currentTimeMillis()); //更新系统时间
                num--;
                if(num<+0){
                    break;
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
