package com.msj.reflection;

//获得系统的类加载器
public class Test07 {
    public static void main(String[] args) throws ClassNotFoundException {
        //获取系统类的加载器
        ClassLoader systemLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemLoader);

        //获得系统类加载器的父类：扩展类加载器
        ClassLoader parent = ClassLoader.getSystemClassLoader().getParent();
        System.out.println(parent);

        //获得扩展类加载器的父类加载器-->根加载器（C/c++编写）:由于是c/c++编写的，所以Java无法直接获取到，返回为空
        ClassLoader rootParent = parent.getParent();
        System.out.println(rootParent);

        //测试当前类是那个加载器加载的
        ClassLoader classLoader = Class.forName("com.msj.reflection.Test07").getClassLoader();
        System.out.println(classLoader);

        //测试jdk内置类是谁加载的（根加载器）
        ClassLoader classLoader1 = Class.forName("java.lang.Object").getClassLoader();
        System.out.println(classLoader1);

        //如何获取系统类加载器可以加载的路径
        String path = System.getProperty("java.class.path");
        System.out.println(path);

        /*
        * 双亲委派机制：如果自己定义一个类，加载器会一级一级往上找，先去用户类加载器看，再去扩展类加载器，最后去跟加载器，如果
        * 上级加载器有自己定义的类，则自己的定义的类无法运行（保证安全性），如自己定义个java.lang.String则自己定义的这个包无法运行
        * */
        /*
        * D:\Software\devolop\Jdk8\jre\lib\charsets.jar;
        * D:\Software\devolop\Jdk8\jre\lib\deploy.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\access-bridge-64.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\cldrdata.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\dnsns.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\jaccess.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\jfxrt.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\localedata.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\nashorn.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\sunec.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\sunjce_provider.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\sunmscapi.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\sunpkcs11.jar;
        * D:\Software\devolop\Jdk8\jre\lib\ext\zipfs.jar;
        * D:\Software\devolop\Jdk8\jre\lib\javaws.jar;
        * D:\Software\devolop\Jdk8\jre\lib\jce.jar;
        * D:\Software\devolop\Jdk8\jre\lib\jfr.jar;
        * D:\Software\devolop\Jdk8\jre\lib\jfxswt.jar;
        * D:\Software\devolop\Jdk8\jre\lib\jsse.jar;
        * D:\Software\devolop\Jdk8\jre\lib\management-agent.jar;
        * D:\Software\devolop\Jdk8\jre\lib\plugin.jar;
        * D:\Software\devolop\Jdk8\jre\lib\resources.jar;
        * D:\Software\devolop\Jdk8\jre\lib\rt.jar;
        * 自己工作环境的目录
        * E:\workspace\IDEA\SpringBoot\Thread\JavaThread\JUC\target\classes;
        * D:\Software\devolop\IDEA\IntelliJ IDEA 2020.3\lib\idea_rt.jar
         * */
    }
}
