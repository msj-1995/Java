package com.msj.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
// 开启Eureka的服务:服务端的启动类，它可以接收别人注册进来
@EnableEurekaServer
public class EurekaConfigServer_7001 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaConfigServer_7001.class, args);
    }
}
