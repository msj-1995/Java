package com.msj.log;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class AfterLog implements AfterReturningAdvice {
    /*第一个参数Object o:返回值
    可以修改为:Object returnValue,这里就改为了
    这种形式，方便阅读
    其余参数与before中的参数代表的意义一致
    */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] objects, Object o1) throws Throwable {
        System.out.println("执行了对象" + o1.getClass().getName() + "中的" + method.getName() + "方法,返回结果为：" + returnValue);
    }
}
