package com.msj.demo02;

public class UserServiceProxy implements UserService{
    //使用组合引入UserServiceImpl;
    private UserServiceImpl userService;

    //spring建议使用set注入
    public UserServiceImpl getUserService() {
        return userService;
    }

    //使用set注入值
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    //真实的业务还是UserService中的方法在做
    @Override
    public void add() {
        log("add");
        userService.add();
    }

    @Override
    public void delete() {
        log("delete");
        userService.delete();
    }

    @Override
    public void update() {
        log("update");
        userService.update();
    }

    @Override
    public void query() {
        log("query");
        userService.query();
    }

    //公共的方法：例如打印日志
    public void log(String msg){
        System.out.println("使用了" + msg + "方法");
    }
}
