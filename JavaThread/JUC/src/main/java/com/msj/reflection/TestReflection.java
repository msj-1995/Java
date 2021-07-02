package com.msj.reflection;

public class TestReflection {
    public static void main(String[] args) throws ClassNotFoundException {
        //通过反射获取类的class对象
        Class c1 = Class.forName("com.msj.reflection.User");
        System.out.println(c1);

        Class c2 = Class.forName("com.msj.reflection.User");
        Class c3 = Class.forName("com.msj.reflection.User");
        Class c4 = Class.forName("com.msj.reflection.User");
        //查看获得的多个对象是否相等（通过查看hashCode是否相等）
        System.out.println(c1.hashCode());
        System.out.println(c2.hashCode());
        System.out.println(c3.hashCode());
        System.out.println(c4.hashCode());
        //通过打印知，通过反射获取到的素有类都想通，因为一个内存中只用一个class对象
        //一个类被加载后，类的整个结构都会被封装在Class对象中
    }
}

//实体类 pojo  entity
class User{
    private String name;
    private int id;
    private int age;

    public User() {
    }

    public User(String name, int id, int age) {
        this.name = name;
        this.id = id;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                '}';
    }
}
