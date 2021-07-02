package com.msj.demo;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

/*
*推到lamda表达式
* */
public class TestLambda {
    //静态内部类
    static class Like2 implements ILike{
        @Override
        public void lambda() {
            System.out.println("I like you-->lambda2");
        }
    }

    public static void main(String[] args) {
        //传统方式使用接口
        ILike like = new Like();
        like.lambda();

        //静态内部类方式
        ILike like1 = new Like2();
        like1.lambda();

        //局部内部类
        class Like3 implements ILike{
            @Override
            public void lambda() {
                System.out.println("I like you-->lambda3");
            }
        }

        like = new Like3();
        like.lambda();

        //4、匿名内部类简化,没有类的名称，必须借助接口或者父类
        like = new ILike(){
            @Override
            public void lambda() {
                System.out.println("i like lambda4");
            }
        };
        like.lambda();

        //5、使用lambda简化like
        like = ()->{
            System.out.println("i like lambda5");
        };
        like.lambda();

    }

}

//定义一个函数式接口
interface  ILike{
    void lambda();
}

//传统类
class Like implements ILike{
    @Override
    public void lambda() {
        System.out.println("I like you-->lambda");
    }
}
