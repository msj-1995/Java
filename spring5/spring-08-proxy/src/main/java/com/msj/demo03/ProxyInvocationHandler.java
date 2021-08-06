package com.msj.demo03;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//等会我们会用这个类，自动生成代理类
public class ProxyInvocationHandler implements InvocationHandler {
    //被代理的接口（即Proxy.newProxyInstance第二个参数），使用组合得到
    private Rent rent;
    //set注入值：
    public void setRent(Rent rent){
        this.rent = rent;
    }

    //生成得到代理类
    /*
    Proxy.newProxyInstance(Foo.class.getClassLoader(),new Class<?>[] { Foo.class },handler);
    第一个参数：类加载器：通过反射拿到
    第二个参数：interface: class(接口），（即被代理的接口类）
    第三个参数：invocationHandler,它实现了InvocationHandler接口，即本身就是
    一个InvocationHandler，所以第三个参数写this即可
    * */
    public Object getProxy(){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),rent.getClass().getInterfaces(),this);
    }

    //处理代理实例，并返回结果
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //动态代理的本质，就是使用反射机制实现
        /*下面两个代理类的公共方法，只是一段处理程序，
        需要加到invoke处理程序中中处理后才能使用*/
        seeHouse();
        Object result = method.invoke(rent,args);
        fare();
        return result;
    }

    //代理类的公共方法：看房
    public void seeHouse(){
        System.out.println("中介带你看房");
    }

    //代理类的公共方法：收费呢
    public void fare(){
        System.out.println("收中介费");
    }
}
