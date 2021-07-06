package com.msj.pojo;

public class UserTwo {
    private String name;

    public UserTwo(){
        System.out.println("UserTwo的无参构被初始化了!");
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void show(){
        System.out.println("name = " + name);
    }
}
