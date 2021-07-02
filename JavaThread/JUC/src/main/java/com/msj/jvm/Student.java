package com.msj.jvm;
public class Student {
    @Override
    public String toString() {
        return "hello";
    }

    public static void main(String[] args) {
        Student student = new Student();
        //查看是在那个加载器中加载的 AppClassLoader
        System.out.println(student.getClass().getClassLoader());
        System.out.println(student.toString());
    }
}