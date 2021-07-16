package com.msj.service;

public class UserServiceImpl implements UserService {
    public void add(){
        System.out.println("增加了一个用户");
    }

    @Override
    public void delete() {
        System.out.println("删除了一个用户");
    }

    @Override
    public void update() {
        System.out.println("更新了用户的信息");
    }

    @Override
    public void select() {
        System.out.println("查询了用户信息");
    }
}
