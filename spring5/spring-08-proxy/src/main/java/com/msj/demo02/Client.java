package com.msj.demo02;

public class Client {
    public static void main(String[] args) {
        /*UserServiceImpl userService = new UserServiceImpl();
        userService.add();*/
        UserServiceImpl userService = new UserServiceImpl();
        UserServiceProxy proxy = new UserServiceProxy();
        proxy.setUserService(userService);

        //使用代理中的方法
        proxy.delete();
    }
}
