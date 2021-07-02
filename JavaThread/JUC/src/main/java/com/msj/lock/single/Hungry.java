package com.msj.lock.single;

//饿汉式单例
public class Hungry {
    //饿汉式浪费内存，一加载类就初始化类对象，加入这个对象中由很多占内存的数组，而数组中由没有初始化东西（即空间被占用而未被使用），就会造成内存的浪费
    private byte[] data1 = new byte[1024*1024];
    private byte[] data2 = new byte[1024*1024];
    private byte[] data3 = new byte[1024*1024];
    private byte[] data4 = new byte[1024*1024];
    //饿汉式：类一启动就加载了对象
    private final static Hungry HUNGRY = new Hungry();
    private Hungry(){

    }

    public static Hungry getInstance(){
        return HUNGRY;
    }
}
