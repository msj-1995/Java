package com.msj.log;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

//前置日志：实现MethodBeforeAdvice即可
public class Log implements MethodBeforeAdvice {
    /*
    参数：
    * method:要执行的目标对象的方法
    * objects:参数列表
    * o:目标对象，target
    * */
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println(o.getClass().getName() + "的" + method.getName() + "方法被执行了");
    }
}
