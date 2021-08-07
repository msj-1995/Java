package com.msj.springcloud.controller;

import com.msj.springcloud.api.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class DeptConsumerController {
    // 注入RestTemplate 参数： (url, 实体：Map, Class<T> responseType),responseType即请求后返回的数据类型，注意是class对象
    @Autowired
    private RestTemplate restTemplate; // 提供多种便捷访问远程http服务的方法，简单的Restful服务模板

    // 请求地址的前缀
    // private static final String REST_URL_PREFIX = "http://localhost:8001";
    // 使用Ribbon后我们这里的地址应该是一个变量，通过服务名来访问
    private static final String REST_URL_PREFIX = "http://springcloud-provider-dept";

    @RequestMapping("/consumer/dept/add")
    public boolean add(Dept dept) {
        return restTemplate.postForObject(REST_URL_PREFIX + "/dept/add", dept, Boolean.class);
    }

    @RequestMapping("/consumer/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id) {
        // 可以使用getForObject，也可以使用getForEntity
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/get/" + id, Dept.class);
    }

    @RequestMapping("/consumer/dept/list")
    public List<Dept> list() {
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/list", List.class);
    }
}
