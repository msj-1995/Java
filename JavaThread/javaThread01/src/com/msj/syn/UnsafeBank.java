package com.msj.syn;

//不安全的取钱 两个人去银行取钱，账户
public class UnsafeBank {
    public static void main(String[] args) {
        Account account = new Account(100,"结婚基金");
        Drawing you = new Drawing(account,50,"你");
        Drawing girlFriend = new Drawing(account,100,"女朋友");
        //两条线程都启动
        you.start();
        girlFriend.start();
    }
}

//账户
class Account{
    int money; //余额
    String name; //账户
    public Account(int money,String name){
        this.money = money;
        this.name = name;
    }
}

//银行 : 模拟取款
class Drawing extends Thread{
    Account account; //账户
    //取了多少钱
    int drawingMoney;
    //现在手里有多少钱
    int nowMoney;
    public Drawing(Account account,int drawingMoney,String name){
        //线程名字
        super(name);
        this.account = account;
        this.drawingMoney = drawingMoney;
    }
    //取钱

    @Override
    public synchronized void run() {
        synchronized (account){
            //判断有没有钱
            if(account.money-drawingMoney<0){
                System.out.println("钱不够，取不了");
                return;
            }
            //模拟延时
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            account.money = account.money - drawingMoney;
            //你手里的钱
            nowMoney = nowMoney + drawingMoney;
            System.out.println(account.name + " 余额为： " +account.money );
            //this.getName()等价于Thread.currentThread().getName(),因为这里继承了Thread,this就代表当前线程
            System.out.println(this.getName()+"手里的钱为：" + nowMoney);
        }
    }
}
