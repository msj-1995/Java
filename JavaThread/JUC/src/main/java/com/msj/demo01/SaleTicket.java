package com.msj.demo01;

//基本的卖票列子
/*
* 正真的线程开发（公司中的开发）,降低耦合类
* 线程就是一个单独的资源类，没有任何的附属操作
* 1、属性 方法
* */
public class SaleTicket {
    public static void main(String[] args) {
        //资源类
        Ticket ticket = new Ticket();
        //并发：多线程操作同一个资源类(把资源类丢入线程即可）
        //@FunctionalInterface
        //public interface Runnable函数式接口 jdk1.8 lamdba表达式
        new Thread(()->{
            for (int i = 1; i < 40; i++) {
                ticket.sale();
            }
        },"A").start();
        new Thread(()->{
            for (int i = 1; i < 40; i++) {
                ticket.sale();
            }
        },"B").start();
        new Thread(()->{
            for (int i = 1; i < 40; i++) {
                ticket.sale();
            }
        },"C").start();
    }
}

//卖票的资源类
class Ticket{
    //属性，方法
    private int number = 50;

    //卖票的方式
    public synchronized void sale(){
        if(number>0){
            System.out.println(Thread.currentThread().getName()+ "-->卖出了第 " + number-- + " 张票，剩余" + number +"张票");
        }
    }

}
