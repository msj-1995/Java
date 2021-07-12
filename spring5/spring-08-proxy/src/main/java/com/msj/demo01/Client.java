package com.msj.demo01;

//真实角色：租房子的人
public class Client {
    public static void main(String[] args) {
        //代理模式
        //房东要租房子
        Host host = new Host();

        //代理 中介帮房东租房子，同时代理角色一般会有一些附属操作
        Proxy proxy = new Proxy(host);
        //通过代理租房子，客户不用面对房东
        proxy.rent();
        //中介的附属操作
        proxy.heTong();
    }
}
