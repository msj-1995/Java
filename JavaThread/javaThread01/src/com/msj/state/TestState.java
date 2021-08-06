package com.msj.state;

//观察测试线程的状态
public class TestState {


    public static void main(String[] args) throws InterruptedException {
        //线程可以使用Lambda表达式
        Thread thread = new Thread(()->{
            for(int i=0;i<5;i++){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println("线程体");
        });

        //观察状态
        Thread.State state = thread.getState();
        System.out.println(state);  //NEW 因为现在的线程刚被创建，还没有执行

        //观察启动后
        thread.start(); //启动线程
        state = thread.getState();
        System.out.println(state); //Run

        //只要线程不终止，就一致输出状态
        while(state!=Thread.State.TERMINATED){
            Thread.sleep(100);
            state = thread.getState(); //更新状态
            System.out.println(state);

        }
    }
}
