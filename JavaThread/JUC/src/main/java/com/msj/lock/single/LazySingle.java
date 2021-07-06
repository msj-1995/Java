package com.msj.lock.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

//懒汉式单列：用的时候才去创建对象
public class LazySingle {
    private volatile static LazySingle lazySingle;
    //使用一个标志位
    private static boolean msj = false;

    private LazySingle(){
        synchronized (LazySingle.class){
            if(msj==false){
                msj = true;
            }else{
                //如果对象已经存在就抛出一个异常(如果对象存在，信号值msj会变为true，则执行下面的代码
                throw new RuntimeException("不要试图使用反射破环单例");
            }
        }
    }

    public static LazySingle getInstance(){
        //双重检测锁模式的懒汉式单例模式，又叫DCL模式
        if(lazySingle==null){
            //给LazySingle这个类上锁
            synchronized (LazySingle.class){
                if(lazySingle==null){
                    lazySingle = new LazySingle();
                }
            }
        }
        return lazySingle;
    }

    //反射
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        //LazySingle lazySingle = LazySingle.getInstance();
        //获得字段
        Field msj = LazySingle.class.getDeclaredField("msj");
        //破环msj的私有权限
        msj.setAccessible(true);
        //通过反射破环单例 获得空参构造器
        Constructor<LazySingle> declaredConstructor = LazySingle.class.getDeclaredConstructor(null);
        //无视私有属性
        declaredConstructor.setAccessible(true);
        //通过反射创建对象
        LazySingle lazySingle1 = declaredConstructor.newInstance();
        //把msj的值改为false
        msj.set(lazySingle1,false);
        LazySingle lazySingle2 = declaredConstructor.newInstance();
        //查看两个对象是否相同
        System.out.println(lazySingle1);
        System.out.println(lazySingle2);
        System.out.println(lazySingle1==lazySingle2);

    }
}
