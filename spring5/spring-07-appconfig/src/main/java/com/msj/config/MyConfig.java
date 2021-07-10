package com.msj.config;

import com.msj.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/*
@Configuration和@Bean的使用相当于
<beans>
    <bean id="" class=""/>
</beans>
@Configuration本身也是一个Component组件（可以点进去看源码），
这个注解也会被spring容器托管
@Configuration代表这是一个配置类，就和我们之前的beans.xml是一样的
* */

@Configuration
/*
也可以显示的定义扫描：扫描标志
@Component("com.msj.pojo");也可以不写
* */
@Component("com.msj.pojo")
//引入另一个配置类
@Import(MyConfig2.class)
public class MyConfig {
    /*
    注册一个bean,就相当于我们之前写的一个bean标签
    这个方法的名字，就相当于bean标签中的id属性
    这个方法的返回值，就相当于bean标签中的class属性
    * */
    @Bean
    //返回一个User对象，因为我们要实现的是User的注解
    public User getUser(){
        return new User();//就是返回要注入到bean的对象！
    }
}
