package com.msj.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
// 开启Feign:它会使指定的包下的@FeignClient生效，这里我们从pox.xml中导入了api，所以api服务下的service中的类的FeignClient也会生效。
@EnableFeignClients(basePackages = {"com.msj.springcloud"})
@ComponentScan("com.msj.springcloud")
public class FeignDeptConsumer_80 {
    public static void main(String[] args) {
        SpringApplication.run(FeignDeptConsumer_80.class, args);
    }
}
