package com.msj.annotation;

import java.lang.annotation.*;

//测试元注解
public class Test01 {
    @MyAnnotation
    public void test(){}
}


//定义一个注解：一个.java文件下只能有一个public类，所以我们直接使用内部类的方式定义（不需要加修饰符）
//TYPE：表示可以放在类上
@Target(value= {ElementType.METHOD,ElementType.TYPE})
//表示我们的注解在什么地方有效:表示运行时也有效
//runtime(运行时）>class(编译成class文件）>source(源码）
@Retention(value= RetentionPolicy.RUNTIME)
//表示是否将我们的注解生成在JavaDoc中
@Documented
//表示子类可以继承父类的注解
@Inherited
@interface MyAnnotation{

}