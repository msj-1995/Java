package com.msj.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {
    //@Async告诉spring这是一个异步方法
    @Async
    public void hello(){
        try{
            //休眠3秒
            Thread.sleep(3000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("正在处理数据");
    }
}
