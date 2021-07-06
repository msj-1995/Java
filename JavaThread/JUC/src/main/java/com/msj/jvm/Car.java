package com.msj.jvm;

public class Car {
    public int age;
    public static void main(String[] args) {
        //类是模板，全局唯一
        Class<Car> carClass = Car.class;

        //实例化对象 这三个对象的名字car1,car2,car3（引用）在栈中，真正的实例数据在堆中
        Car car1 = new Car();
        Car car2 = new Car();
        Car car3 = new Car();

        //赋值
        car1.age= 1;
        car2.age= 2;
        car3.age= 3;


        //让car1(实例）再变回class,三个实例指向的类模板相同，即aClass1=aClass2=aClass3
        Class<? extends Car> aClass1 = car1.getClass();
        Class<? extends Car> aClass2 = car2.getClass();
        Class<? extends Car> aClass3 = car3.getClass();

        System.out.println(aClass1);  //Class Car
        System.out.println(aClass2);  //Class Car
        System.out.println(aClass3);  //Class Car
        //也可以通过查看他们的hashCode(也是一样的）
        System.out.println(aClass1.hashCode());
        System.out.println(aClass2.hashCode());
        System.out.println(aClass3.hashCode());

        //class类模板在回到Class Loader:类模板名.getClassLoader() 也是一样的
        ClassLoader classLoader1 = aClass1.getClassLoader();
        ClassLoader classLoader2 = aClass1.getClassLoader();
        ClassLoader classLoader3 = aClass1.getClassLoader();

        //AppClassLoader 属于应用程序类加载器
        System.out.println(classLoader1);

        System.out.println(classLoader1.hashCode());
        System.out.println(classLoader2.hashCode());
        System.out.println(classLoader3.hashCode());

        //获得classLoader1的父加载器:ExtClassLoader 扩展类加载器 \jre\lib\ext下
        System.out.println(classLoader1.getParent());
        //获得classLoader1的父加载器的父加载器：null，java获取不到，在rt.jar
        System.out.println(classLoader1.getParent().getParent());
    }
}
