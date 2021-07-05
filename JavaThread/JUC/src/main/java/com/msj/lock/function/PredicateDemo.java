package com.msj.lock.function;

import java.util.function.Predicate;

public class PredicateDemo {
    public static void main(String[] args) {
        //判断字符串是否为空:为空返回true,否则返回false
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String str) {
                return str.isEmpty();
            }
        };
        System.out.println(predicate.test("123"));

        //lambda简化
        Predicate<String> predicate1 = (str)->{
            return str.isEmpty();
        };
        System.out.println(predicate1.test(""));
    }
}
