package com.msj.demo;

public class TestLambda2 {
    static class Love2 implements ILove{
        @Override
        public void love(int a) {
            System.out.println("i love you -->" + a);
        }
    }
    public static void main(String[] args) {
        //传统
        ILove love = new Love();
        love.love(1);

        //静态内部类
        ILove love1 = new Love2();
        love1.love(2);

        //局部内部类
        class Love3 implements ILove{
            @Override
            public void love(int a) {
                System.out.println("i love you -->" + a);
            }
        }
        love = new Love3();
        love.love(3);

        //匿名内部类
        love = new ILove() {
            @Override
            public void love(int a) {
                System.out.println("i love you -->" + a);
            }
        };
        love.love(4);

        //lambda表达式
        ILove love5 = (int a)->{
            System.out.println("i love you -->" + a);
        };
        love5.love(5);

        //简化一：去掉参数类型
        love5 = (a)->{
            System.out.println("i love you -->" + a);
        };
        love5.love(520);

        //简化二：去掉括号和参数类型，只留下参数名
        love5 = a->{
            System.out.println("i love you -->" + a);
        };
        love5.love(1314);

        //简化三，在去掉花括号
        love5 = a-> System.out.println("i love you -->" + a);

        love5.love(521);
    }
}

interface ILove{
    void love(int a);
}

class Love implements ILove{
    @Override
    public void love(int a) {
        System.out.println("I love you -->" + a);
    }
}