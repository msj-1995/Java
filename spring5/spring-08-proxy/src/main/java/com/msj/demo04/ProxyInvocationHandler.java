package com.msj.demo04;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//等会我们会用这个类，自动生成代理类
public class ProxyInvocationHandler implements InvocationHandler {
    //被代理的接口:改为Object，可以是任意的对象
    private Object target;
    //set注入值：
    public void setObject(Object target){
        this.target = target;
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
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }

    //处理代理实例，并返回结果
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //处理代理实例之前打印日志:通过反射拿到执行方法的名字
        log(method.getName());

        //动态代理的本质，就是使用反射机制实现
        Object result = method.invoke(target,args);
        return result;
    }

    //代理类的公共方法：日志
    public void log(String msg){
        System.out.println("执行了" + msg + "方法");
    }
}
