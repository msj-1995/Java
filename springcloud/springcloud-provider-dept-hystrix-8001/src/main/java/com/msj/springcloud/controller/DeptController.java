package com.msj.springcloud.controller;

import com.msj.springcloud.api.pojo.Dept;
import com.msj.springcloud.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

// 提供Restful服务：数据直接使用json格式传送
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;

    @GetMapping("/dept/get/{id}")
    @HystrixCommand(fallbackMethod = "hystrixGet")
    public Dept get(@PathVariable("id") Long id) {
        Dept dept = deptService.queryById(id);
        // 用户不存在，则抛出一个异常
        if(dept == null) {
            throw new RuntimeException("id=>" + id + "，不存在该用户，或者信息无法找到......");
        }
        return dept;
    }

    // 如果出现异常：则调用备选方案
    public Dept hystrixGet(@PathVariable("id") Long id) {
        return new Dept()
                .setDeptno(id)
                .setDname("id=>" + id + "没有对应的信息，null--@Hystrrix")
                .setDb_source("没有这个数据库");
    }
}
