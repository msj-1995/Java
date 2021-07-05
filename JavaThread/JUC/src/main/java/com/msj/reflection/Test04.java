package com.msj.reflection;

import java.lang.annotation.ElementType;

//所有类型的Class
public class Test04 {
    public static void main(String[] args) {
        //类的Class实列
        Class c1 = Object.class;

        //Comparable:接口
        Class c2 = Comparable.class;

        //数组
        Class c3 = String[].class;

        //二维数组
        Class c4 = int[][].class;

        //注解类型
        Class c5 = Override.class;

        //枚举
        Class c6 = ElementType.class;

        //基本数据类型
        Class c7 = Integer.class;

        //void:空类型
        Class c8 = void.class;

        //Class类
        Class c9 = Class.class;

        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);
        System.out.println(c4);
        System.out.println(c5);
        System.out.println(c6);
        System.out.println(c7);
        System.out.println(c8);
        System.out.println(c9);

        int[] a = new int[10];
        int[] b = new int[100];
        //查看数组长度不同的数组，是否是属于同一个类
        //通过打印知：只要元素的类型与维度一样，就属于一个Class对象
        System.out.println(a.getClass().hashCode());
        System.out.println(b.getClass().hashCode());
    }
}
