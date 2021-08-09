package com.msj.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigClientController {
    // 通过远程配置文件注入值，看看能不能拿到
    // 注入application.name的值
    @Value("${spring.application.name}")
    private String applicationName;
    // 我们远程配置了eureka，而这里并没有配置，看看能不能拿到
    @Value("${eureka.client.service-url.defaultZone")
    private String eurekaServer;

    @Value("${server.port}")
    private String port;

    @RequestMapping("/config")
    public String getConfig() {
        return "application: " + applicationName +
                "eurekaServer: " + eurekaServer +
                "port: " + port;
    }
}
