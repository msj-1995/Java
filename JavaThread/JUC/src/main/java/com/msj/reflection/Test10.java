package com.msj.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//性能测试
public class Test10 {
    //普通方式调用
    public static void test1(){
        User user = new User();
        long startTime = System.currentTimeMillis();
        //操作一千万次的时间
        for(int i=0;i<1000000000;i++){
            user.getName();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("普通方式执行10亿次时间：" + (endTime-startTime) + "ms");
    }

    //反射方式
    public static void test2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User user = new User();
        Class c1 = user.getClass();
        Method getName = c1.getDeclaredMethod("getName",null);
        long startTime = System.currentTimeMillis();
        //操作一千万次的时间
        for(int i=0;i<1000000000;i++){
            getName.invoke(user,null);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("反射方式执行10亿次时间：" + (endTime-startTime) + "ms");
    }

    //反射方式 关闭检测
    public static void test3() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User user = new User();
        Class c1 = user.getClass();
        Method getName = c1.getDeclaredMethod("getName",null);
        getName.setAccessible(true);
        long startTime = System.currentTimeMillis();
        //操作一千万次的时间
        for(int i=0;i<1000000000;i++){
            getName.invoke(user,null);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("反射方式执行10亿次时间,关闭安全检测后：" + (endTime-startTime) + "ms");
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        test1();
        test2();
        test3();
    }
}
