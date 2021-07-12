package com.msj.demo04;

import com.msj.demo02.UserService;
import com.msj.demo02.UserServiceImpl;

public class Client {
    public static void main(String[] args) {
        //真实角色
        UserServiceImpl userService = new UserServiceImpl();

        //代理角色：不存在，需要动态得到它
        ProxyInvocationHandler pih = new ProxyInvocationHandler();

        //设置代理的对象
        pih.setObject(userService);

        //动态生成代理类
        UserService proxy =  (UserService) pih.getProxy();

        //测试能真实执行UserServiceImpl中的方法
        proxy.delete();
    }
}
