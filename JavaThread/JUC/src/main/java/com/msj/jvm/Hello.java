package com.msj.jvm;

import java.util.Random;

public class Hello {
    public static void main(String[] args) {
        String str = "msj love mym";
        while(true){
            str += new Random().nextInt(88888888) + new Random().nextInt(9999999);
        }
    }
}
