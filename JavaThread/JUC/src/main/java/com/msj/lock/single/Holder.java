package com.msj.lock.single;

//静态内部类
public class Holder {
    private Holder(){}
    //静态内部类
    public static class InnerClass{
        private static final Holder HOLDER = new Holder();
    }

    //使用静态内部类创建对象
    public static Holder getInstance(){
        return InnerClass.HOLDER;
    }
}
