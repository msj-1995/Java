package com.msj.demo;

public class StaticProxy {
    public static void main(String[] args) {
        /*WeddingCompany weddingCompany = new WeddingCompany();
        weddingCompany.setTarget(new You());
        weddingCompany.happyMarry();*/
        //WeddingCompany weddingCompany = new WeddingCompany(new You());
        //weddingCompany.happyMarry();
        new WeddingCompany(new You()).happyMarry();

        //线程
        new Thread(()-> System.out.println("我爱你")).start();
    }
}

//接口：结婚
interface Marry{
    void happyMarry();
}
//真实角色：你去结婚
class You implements Marry{
    @Override
    public void happyMarry(){
        System.out.println("秦老师要结婚了，超开心");
    }
}

//代理角色，帮助你结婚
class WeddingCompany implements Marry{
    //代理角色：聚合到该类下即可
    private Marry target;

    public WeddingCompany(Marry target){
        this.target = target;
    }

    public void setTarget(Marry target) {
        this.target = target;
    }

    @Override
    public void happyMarry() {
        //结婚前
        before();
        this.target.happyMarry();
        //结婚后
        after();
    }

    public void before(){
        System.out.println("结婚前很快乐");
    }

    public void after(){
        System.out.println("结婚后很累");
    }
}
