package com.msj.diy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/*方式三：使用注解实现AOP，注解为@Aspect
@Aspect标注这个类是一个切面
其功能实现的就是xml配置AOP的功能*/
@Aspect
public class AnnotationPointCut {
    /*后置
    @Before("")中需要填入切入点*/
    @Before("execution(* com.msj.service.UserServiceImpl.*(..))")
    public void before(){
        System.out.println("------方法执行前------");
    }

    //后置
    @After("execution(* com.msj.service.UserServiceImpl.*(..))")
    public void after(){
        System.out.println("------方法执行后------");
    }

    /*环绕
    * 在环绕增强中，我们可以给定一个参数，代表我们要获取处理切入的点
    *ProceedingJoinPoint jp:在执行切入的时候，可以得到一些对象，这些
    * 对象我们可以直接拿来用
    * */
    @Around("execution(* com.msj.service.UserServiceImpl.*(..))")
    public void around(ProceedingJoinPoint jp) throws Throwable {
        //环绕前
        System.out.println("环绕前");

        //获得签名
        /*Signature signature = jp.getSignature();
        System.out.println("signature:" + signature);*/

        //执行方法：正真执行方法（即加了这句，切入点的方法才能执行）
        Object proceed = jp.proceed();
        //System.out.println(proceed);

        //环绕后
        System.out.println("环绕后");
    }
}

