package com.msj.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test09 {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        //获得class对象
        Class c1 = Class.forName("com.msj.reflection.User");

        //构造一个对象
        //User user = (User)c1.newInstance();  //本质上是调用了User的无参构造（没有无参构造就不能使用这个方法）
        //System.out.println(user);

        //通过有参创建对象：即构造器创建对象
        Constructor constructor = c1.getDeclaredConstructor(String.class, int.class, int.class);
        User user1 = (User)constructor.newInstance("孟顺建",1002,18);
        System.out.println(user1);

        //通过反射调用方法：通过反射获得一个方法
        Method setName = c1.getDeclaredMethod("setName", String.class);
        //执行方法:第二个参数的可变的参数，也是我们传入的参数 invoke:激活 (对象，“方法的值”)
        setName.invoke(user1,"小马呀");
        System.out.println(user1.getName());

        //通过反射操作属性
        User user2 = (User)c1.newInstance();
        //获得属性
        Field name = c1.getDeclaredField("name");
        //通过set赋值 私有属性不能直接操作，需要关闭检测
        //关闭安全检测name.setAccessible(true);默认为false，开启
        name.setAccessible(true);
        name.set(user2,"狂神2");
        System.out.println(user2.getName());

    }
}
