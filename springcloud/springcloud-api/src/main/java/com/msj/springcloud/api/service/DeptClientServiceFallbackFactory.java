package com.msj.springcloud.api.service;

import com.msj.springcloud.api.pojo.Dept;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// 服务降级
@Component
public class DeptClientServiceFallbackFactory implements FallbackFactory {
    @Override
    // 这里是处理一个类，要处理哪个类的回调，就返回哪个类，由于这里的DeptClientService是一个接口，不能new，就不能返回该类的对象，所以我们要先实现DeptClientService接口，或者是重写该接口中的方法,如下
    public DeptClientService create(Throwable throwable) {
        return new DeptClientService() {
            @Override
            public Dept queryById(Long id) {
                // 这里的操作跟Hystrix中的一样
                return new Dept()
                        .setDeptno(id)
                        .setDname("id=>" + id + "，没有对应的信息，客户端提供了降级的信息，该服务现在已经被关闭，不可用......")
                        .setDb_source("no Database");
            }

            @Override
            public List<Dept> queryAll() {
                List<Dept> list = new ArrayList<>();
                list.add(new Dept()
                        .setDeptno(1L)
                        .setDname("queryAll=>没有对应的信息，客户端提供了降级的信息，该服务现在已经被关闭，不可用......")
                        .setDb_source("no Database"));
                return list;
            }

            @Override
            public boolean addDept(Dept dept) {
                return false;
            }
        };
    }
}
