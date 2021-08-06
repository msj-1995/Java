package com.msj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//自定义注解
public class Test03 {
    //可以显示赋值，如果没有默认值，我们就必须给注解赋值
    @MyAnnotation2(name="msj",school={"西北大学","云南大学"})
    public void test1(){}

    //注解中只有一参数，且为默认的value时，使用时可以省略参数名，直接写参数
    @MyAnnotation3("hello")
    public void test2(){}
}


@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation2{
    //注解的参数：参数类型+参数名():默认值为空（或者其他）
    String name() default "";
    int age() default 0;
    //如果默认值为-1，代表不存在
    int id() default -1;

    //数组
    String[] school();

    //数组默认
    String[] hobbies() default {"篮球","排球"};
}

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface  MyAnnotation3{
    //只有一个值的情况下，建议使用value命名
    String value();
}
