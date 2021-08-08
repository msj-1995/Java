package com.msj.springcloud;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

// 启动类
@SpringBootApplication
@EnableEurekaClient
// 服务发现
@EnableDiscoveryClient
// 开启Hystrix服务
@EnableCircuitBreaker
public class DeptProviderHystrix_8001 {
    public static void main(String[] args) {
        SpringApplication.run(DeptProviderHystrix_8001.class, args);
    }

    // 增加一个servlet
    @Bean
    public ServletRegistrationBean hystrixMetricsStreamServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean<>(new HystrixMetricsStreamServlet());
        // 添加监控的路径:/actuator/hystrix.stream,我们只要填这个路径就行了
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        return registrationBean;
    }
}
