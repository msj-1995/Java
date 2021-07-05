package com.msj.lock.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/*
* 1分钟内完成此题，只能用一行代码实现
* 现有5个用户，筛选：
* 1.ID必须是偶数
* 2.年龄必须大于23岁
* 3.用户名转为大写字母
* 4.用户名字母倒着排序
* 5.只能输出一个用户
* */
public class Test {
    public static void main(String[] args) {
        User u1 = new User(1,"msj",21);
        User u2 = new User(2,"mym",22);
        User u3 = new User(3,"xhm",23);
        User u4 = new User(4,"ly",24);
        User u5 = new User(5,"yyj",25);
        User u6 = new User(6,"yj",26);
        //集合就是存储
        List<User> users = Arrays.asList(u1,u2,u3,u4,u5,u6);
        //计算交给流：把集合转换为流 list.stream()
        //filter：过滤，他要接收一个参数，该参数属于断定型接口的函数式接口 u.getId()%2==0表示id为偶数的用护
        users.stream().filter(u->{return u.getId()%2==0;})
                .filter(u->{return u.getAge()>23;})
                //map(u->{return u.getName().toUpperCase();})：传入的形式参数为User类型的u,返回值为String
                .map(u->{return u.getName().toUpperCase();})
                //用户名字母倒着排序 sorted():默认正序
                .sorted((uu1,uu2)->{return uu2.compareTo(uu1);})
                //只输出一个用户
                .limit(1)
                .forEach(System.out::println);
    }

}
