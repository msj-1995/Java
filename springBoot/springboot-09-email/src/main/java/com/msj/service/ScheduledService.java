package com.msj.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {
    //在一个特定的时间执行 Timer
    //cron表达式 0 * * * * 0-7 秒 分 时 日 月 周几~
    //0 * * * * 0-7 代表0-7每一天的任何时候的（月，日，时，分）的第0秒执行下面的代码
    @Scheduled(cron="0 * * * * 0-7")
    public void hello(){
        System.out.println("hello,你被执行了~");
    }
}
