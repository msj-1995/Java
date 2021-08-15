package com.msj.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //首页所有人都可以访问，功能只有对应有权限的人才能访问  链式编程
        //认证请求匹配"/"所有人都能访问（permitAll()）,permitAll()后面还可以匹配其他请求
        //hasRole:指定的角色才可以访问
        http.authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");

        //没有权限设置默认到登录页面
       //http.formLogin();

        //定制登录页 点击登录跳转到login页面
        //http.formLogin().loginPage("/toLogin");

        //定制登录页 前后端请求不一样，需指定指定登录认证的页面
        //http.formLogin().loginPage("/toLogin").loginProcessingUrl("/login");

        //定制登录页 前后端请求不一样，且参数不是默认的username和password时
        http.formLogin().loginPage("/toLogin").usernameParameter("user").passwordParameter("pwd").loginProcessingUrl("/login");

        //开启注销功能
        //http.logout();

        //注销，清空所有cookie，并注销session ,一般都不会这样做
        //http.logout().deleteCookies("remove").invalidateHttpSession(true);

        //登录成功后跳转到首页
        //防止网站攻击 get不安全，post比较安全
        http.csrf().disable();  //关闭跨站防伪请求功能，注销失败的可能原因就是可能这个没关闭
        http.logout().logoutSuccessUrl("/");

        //开启记住我功能 本质就是保存cookie
       //http.rememberMe();

        //开启记住我功能 自定义接收前端参数
        http.rememberMe().rememberMeParameter("remember");
    }

    //认证管理配置 springboot2.1.x可以正常使用
    //在spring Security5.+中提供了很多的加密方式
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //这里可以配置认证规则
        //这里我们在内存中虚拟了一个用户（也可以从数据库中取，由于我们没有练级数据库，所以用虚拟数据）
        //and可以配置多个用户，roles是一个可变长参数
        //使用BCryptPasswordEncoder()加密
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("msj").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2","vip3")
                .and().withUser("root").password(new BCryptPasswordEncoder().encode("1234")).roles("vip1","vip2","vip3")
                .and().withUser("guest").password(new BCryptPasswordEncoder().encode("123")).roles("vip");
    }
}
