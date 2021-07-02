package com.msj.jvm;

public class Demo01 {
    public static void main(String[] args) {
        //返回虚拟机试图使用的最大内存
        long max = Runtime.getRuntime().maxMemory(); //字节:  M(兆) = 1024*1024

        //返回JVM的初始化总内存
        long total = Runtime.getRuntime().totalMemory();

        System.out.println("max= " + max + "字节\t" + (max/(double)1024/1024) + " MB");
        System.out.println("total= " + total + "字节\t" + (total/(double)1024/1024) + " MB");
    }
}
