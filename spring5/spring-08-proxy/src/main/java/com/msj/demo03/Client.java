package com.msj.demo03;

public class Client {
    public static void main(String[] args) {
        //真实角色
        Host host = new Host();

        /*代理角色：现在没有（因为现在只有一个处理程序ProxyInvocationHandler，
        现在我们先要new出处理程序*/

        ProxyInvocationHandler pih = new ProxyInvocationHandler();
        //通过调用程序处理角色来出来我们要调用的接口对象！
        pih.setRent(host);

        //生成代理类,这里的proxy是动态生成的，我们并没有写
        Rent proxy = (Rent)pih.getProxy();

        proxy.rent();
    }
}
