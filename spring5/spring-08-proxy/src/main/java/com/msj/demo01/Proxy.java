package com.msj.demo01;

//代理：帮房东租房子
//可以使用继承和组合：建议，多用组合，少用继承，继承具有局限性
//代理模式也要实现租房子，所以他也要实现Rent（）接口
public class Proxy implements Rent{
    //使用组合：
    private Host host;

    //无参构造
    public Proxy(){}

    //有参构造：方便使用，待会丢那个房东就去，就可以用那个房东
    public Proxy(Host host){
        this.host = host;
    }

    @Override
    public void rent() {
        //调用Host的方法实现租房
        host.rent();
    }

    //中介能做但房东不能做的事：看房子,因为房东只有一套房子
    public void seeHouse(){
        System.out.println("中介带你看房");
    }

    //收中介费
    public void fare(){
        System.out.println("收中介费");
    }

    //签合同
    public void heTong(){
        System.out.println("签合同");
    }
}
