package com.msj.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
在之前写了一个类就要到xml中去注册这个类，现在使用注解开发，
直接使用@Component注解，使这个类变成一个组件就可以了
@Component 组件 使这个类装配到spring容器中
@Component注解等价于：<bean id="user" class="com.msj.pojo.User"/>
因此在Java中getBean()时，可以直接使用这个类的首字母小写的驼峰命名格式的名字
即这里直接getBean("user");即可
* */
@Component
/*标注一个类的作用域：singleto表示是一个单例模式
prototype:原型模式
* */
@Scope("singleton")
public class User {
    /*为了方便测试，这里直接使用public属性
    注入值：使用@value注解，这是spring的注解*/

    public String name;

    public String getName() {
        return name;
    }

    @Value("msj")
    public void setName(String name) {
        this.name = name;
    }
}
