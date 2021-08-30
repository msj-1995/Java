package com.msj.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //下面的三大对象要反着写

    //3、ShiroFilterFactoryBean
    @Bean(name="shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);
        /*
        * 添加shiro的内置过滤器
        * shiro中有如下过滤器
        * anon:无需认证即可访问
        * authc：必须认证才能访问
        * user：必须拥有记住我功能，才能访问
        * perms:拥有对某个资源的权限才能访问
        * */
        //由于filterChainDefinitionMap是链式的，所以我们可以使用LinkedHashMap<>()
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        filterChainDefinitionMap.put("/user/add","authc");
        filterChainDefinitionMap.put("/user/update","authc");
        //filterChainDefinitionMap也支持通配符,如下
        //filterChainDefinitionMap.put("/user/*","authc");

        //授权 表示只有是user用户且有add权限的人才可以访问add请求（没有权限会报401未授权错误）
        filterChainDefinitionMap.put("/user/add","perms[user:add]");
        filterChainDefinitionMap.put("/user/update","perms[user:update]");

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        //如果没有认证：回到登录页面
        bean.setLoginUrl("/toLogin");
        //为授权页面
        bean.setUnauthorizedUrl("/noauth");

        return bean;
    }

    //2、DefaultWebSecurityManager
    //通过@Qualifier("userRealm")注入userRealm
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm 需要注入一个UserRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //1、创建realm对象，需要自定义
    //@Bean(name="userRealm")将userReam交给spring托管，使用name属性指定bean的名字（一般不用，因为方法名就是bean的名字，只不过加了name更清晰而已）
    @Bean(name="userRealm")
    public UserRealm userRealm(){
        return new UserRealm();
    }


    @Bean
    //整合shiroDialect:用来整合shiro thymeleaf
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
