package com.msj.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

//获得类的信息
public class Test08 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {
        Class c1 = Class.forName("com.msj.reflection.User");

        //获得类的名字:全限定名
        System.out.println(c1.getName());
        //获得类的简单名字：只有类名
        System.out.println(c1.getSimpleName());

        //通过对象也可以获得包名和类名
        User user = new User();
        Class c2 = user.getClass();
        //获得类的名字:全限定名
        System.out.println(c2.getName());
        //获得类的简单名字：只有类名
        System.out.println(c2.getSimpleName());

        //获得类的属性:只能获得public属性
        Field[] fields = c1.getFields();
        for (Field field : fields) {
            System.out.println(field);
        }

        System.out.println("==============================");
        //可以获得所有属性
        Field[] declaredFields = c1.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField);
        }

        //获得指定属性
        Field name = c1.getDeclaredField("name");

        System.out.println("-------------------------------");
        //获得类的方法
        Method[] methods = c1.getMethods();  //获得本类及父类的所有public方法
        for (Method method : methods) {
            System.out.println(method);
        }

        System.out.println("+++++++++++++++++++++++++++++++++");
        Method[] declaredMethods = c1.getDeclaredMethods();//获得本类的所有方法（私有方法也可获取）
        for (Method declaredMethod : declaredMethods) {
            System.out.println(declaredMethod);
        }

        //获取指定方法:第二个参数是类型的Class，getName没有类型，所有填入null即可
        Method getName = c1.getMethod("getName", null);
        //获取指定的方法，并传入参数,参数类型为String，传如String.class即可获得参数的Class实列
        Method setName = c1.getMethod("setName", String.class);
        System.out.println(getName);
        System.out.println(setName);
        //需要参数的原因:因为重载的存在

        //获得指定的构造器
        System.out.println("----------------------------------");
        Constructor[] constructors = c1.getConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
        }

        Constructor[] declaredConstructors = c1.getDeclaredConstructors();
        for (Constructor declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor);
        }

        //获得指定的构造器:需要传入参数的Class
        Constructor constructor = c1.getConstructor(String.class, int.class, int.class);
        System.out.println("指定的构造器:" + constructor);
    }
}
