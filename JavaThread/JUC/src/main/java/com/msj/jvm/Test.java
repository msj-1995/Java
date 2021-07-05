package com.msj.jvm;

public class Test {
    public static void main(String[] args) {
        new Test().a();
    }

    public void a(){
        b();
    }

    public void b(){
        a();
    }
}
