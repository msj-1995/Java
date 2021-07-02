package com.msj.lock.function;

import java.util.function.Consumer;

public class ConsumerDemo {
    public static void main(String[] args) {
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("hello,你好呀");

        //lambda简化
        Consumer<String> consumer1 = (str)->{
            System.out.println(str);
        };
        consumer1.accept("hello,hello!");
    }
}
