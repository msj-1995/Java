package com.msj.reflection;

//测试类什么时候初始化
public class Test06 {
    static{
        System.out.println("Main被加载");
    }
    public static void main(String[] args) throws ClassNotFoundException {
        //1.主动引用
        //Son son = new Son();

        //2、反射也会产生主动引用
        //Class.forName("com.msj.reflection.Son");

        //不会产生类的初始化:子类调用父类的静态成员或方法
        //System.out.println(Son.b);

        //数组也不会引起类的初始化
        //Son[] array = new Son[5];

        //调用子类的常量
        System.out.println(Son.M);
    }
}

class Father{
    static int b =2;
    static{
        System.out.println("父类被调用");
    }
}

class Son extends Father{
    static{
        System.out.println("子类被加载");
    }
    static int m = 100;
    //常量池中的数据
    static final int M = 1;
}