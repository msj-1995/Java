package com.msj.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfig {
    //绑定配置文件
    @ConfigurationProperties(prefix="spring.datasource")
    //注入bean
    @Bean
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }

    @Bean
    //druid的后台监控功能 :该配置相当于web.xml ,ServletRegistrationBean
    //因为springboot内置了servlet容器，所以没有web.xml.这是替代方法
    public ServletRegistrationBean statViewServlet(){
        //只要访问druid/*就可以进入后台的监控
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");
        //后台需要有人登录 配置账号密码
        HashMap<String,String> initParameters = new HashMap<>();
        //增加配置 登录的key(loginUsername,loginPassword）是固定的
        initParameters.put("loginUsername","admin");
        initParameters.put("loginPassword","123456");

        //允许谁可以访问 allow为空，所有人都可以访问
        //allow为localhost，只有本机能访问
        //allow为具体的某一个人，只有指定的人的能访问
        initParameters.put("allow","");

        //禁止谁能访问 xxxip为192.168.11.112的禁止访问
        //initParameters.put("xxx","192.168.11.112");



        bean.setInitParameters(initParameters);
        return bean;
    }

    @Bean
    //配置filter
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        //web支持
        bean.setFilter(new WebStatFilter());
        //可以过滤纳西请求
        Map<String,String> initParameters = new HashMap<>();
        //那些不需要过滤 这些东西不进行统计
        initParameters.put("exclusions","*.js,*.css,/druid/*");

        bean.setInitParameters(initParameters);

        return bean;
    }
}
