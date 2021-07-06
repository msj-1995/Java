package com.msj.lock.function;

import java.util.function.Supplier;

public class SupplierDemo {
    public static void main(String[] args) {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 1024;
            }
        };
        System.out.println(supplier.get());

        //lambda简化
        Supplier<Integer> supplier1 = ()->{
            return 1024;
        };
        System.out.println(supplier1.get());
    }
}
