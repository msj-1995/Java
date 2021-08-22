package com.msj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.msj.mapper")
public class SpringbootSpring05MybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSpring05MybatisApplication.class, args);
    }

}
