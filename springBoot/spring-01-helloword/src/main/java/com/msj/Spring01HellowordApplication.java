package com.msj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication标注这个类是一个springboot应用
@SpringBootApplication
public class Spring01HellowordApplication {

    //将springboot应用启动
    public static void main(String[] args) {
        SpringApplication.run(Spring01HellowordApplication.class, args);
    }

}
