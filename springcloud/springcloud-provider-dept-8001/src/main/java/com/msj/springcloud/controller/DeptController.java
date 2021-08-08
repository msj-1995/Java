package com.msj.springcloud.controller;

import com.msj.springcloud.api.pojo.Dept;
import com.msj.springcloud.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 提供Restful服务：数据直接使用json格式传送
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;

    // 获得一些配置信息，得到具体的微服务
    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping("/dept/add")
    public boolean addDept(Dept dept) {
        return deptService.addDept(dept);
    }

    @GetMapping("/dept/get/{id}")
    public Dept queryById(@PathVariable("id") Long id) {

        Dept dept = deptService.queryById(id);
        if(dept == null) {
            throw new RuntimeException("id=>" + id + "，不存在该用户，或者信息无法找到......");
        }
        return dept;
    }

    @GetMapping("/dept/list")
    public List<Dept> queryAll() {
        return deptService.queryAll();
    }

    // 注册进来的微服务，我们可以获取一些消息，可以获取我们配置文件中配置的info
    @GetMapping("/dept/discovery")
    public Object discovery() {
        // 获得微服务列表的清单
        List<String> service = discoveryClient.getServices();
        System.out.println("discover=>service:" + service);

        // 得到一个具体的微服务信息:通过具体的微服务id，也就是applicationName
        List<ServiceInstance> instances = discoveryClient.getInstances("springcloud-provider-dept");
        for(ServiceInstance instance : instances) {
            System.out.println(instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri() + "\t" + instance.getServiceId());
        }
        return this.discoveryClient;
    }

}
