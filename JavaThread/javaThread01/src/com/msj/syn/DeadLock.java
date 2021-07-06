package com.msj.syn;

//死锁：多线程互相抱着对方需要的资源，然后形成僵持
public class DeadLock {
    public static void main(String[] args) {
        Makeup girl1 = new Makeup(0,"灰姑娘");
        Makeup girl2 = new Makeup(1,"白雪公主");

        girl1.start();
        girl2.start();
    }
}

//口红
class Lipstick{

}

//镜子
class Mirror{}

//化妆
class Makeup extends Thread{
    //需要的资源只有一份，用static来保证只有一份
    static Lipstick lipstick = new Lipstick();
    static Mirror mirror = new Mirror();

    int choice;//选择
    String girlName; //使用化妆品的人

    public Makeup(int choice,String girlName){
        this.choice = choice;
        this.girlName = girlName;
    }

    @Override
    public void run() {
        //化妆
        try {
            makeup();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //化妆，互相持有对方的锁，需要拿到对方的资源
    private void makeup() throws InterruptedException {
        if(choice==0){
            synchronized (lipstick){//获得口红的锁
                System.out.println(this.girlName + "获得口红的锁");
                Thread.sleep(1000);
            }
            synchronized (mirror){
                //1秒中后想获得镜子的锁
                System.out.println(this.girlName + "获得镜子的锁");
            }
        }else{
            synchronized (mirror){
                //另一个人获得镜子的锁
                System.out.println(this.girlName + "后的镜子的锁");
                Thread.sleep(2000);
            }
            synchronized (lipstick){
                //2秒后想获得口红的锁
                System.out.println(this.girlName + "后的口红的锁");
            }
        }
    }
}
