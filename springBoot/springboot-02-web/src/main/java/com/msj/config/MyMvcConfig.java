package com.msj.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //访问 / 和 /index会转发到首页（可以注册多个）
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        //配置main.html的映射（如果不配置，登录成功时直接跳转到主页dashboard.html就会显示携带的参数）
        registry.addViewController("/main.html").setViewName("dashboard");
    }

    //将我们配置的LocaleResolver注册到bean中
    @Bean
    public LocaleResolver localeResolver(){
        return new MLocaleResolver();
    }

    //添加拦截器的bean
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*添加拦截器， addPathPatterns("/**")：拦截所有请求
        excludePathPatterns("index.html","/","/user/login");排除首页index.html,首页/,登录页/user/login不用拦截
        静态资源也不用拦截：否则页面不能正常显示"/css/**,/js/**","/img/**"
        */
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/index.html","/",
                "/user/login","/css/**","/js/**","/img/**");
    }
}
