package com.msj.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
// 开启监控页面
@EnableHystrixDashboard
public class DeptConsumerDashBoard_9001 {
    public static void main(String[] args) {
        SpringApplication.run(DeptConsumerDashBoard_9001.class, args);
    }
}
