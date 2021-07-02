package com.msj.lock.function;

import java.util.function.Function;

/*
* Function 函数型接口
* */
public class Demo01 {
    public static void main(String[] args) {
        /*Function function = new Function<String,String>(){
            //工具类 输出传入的参数
            @Override
            public String apply(String str) {
                return str;
            }
        };*/
        //使用lambda表达式简化
        Function function = str-> {
            return str;
        };
        System.out.println(function.apply("abcdefg"));
    }
}
