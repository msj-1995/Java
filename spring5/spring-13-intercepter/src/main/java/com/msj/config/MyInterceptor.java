package com.msj.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class MyInterceptor implements HandlerInterceptor {
    //return true:执行下一个拦截器 放行
    //return false:不执行下一个拦截器 不放行
    //不放行时可以通过response实现重定向和转发到其他地方
    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        System.out.println("=======处理前========");
        return true;
    }

    /*正常情况下只用写preHandle,处理后和清理就不用拦截了，但处理后和清理拦截可以用来拦截日志*/
    @Override
    public void postHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("==========处理后========");
    }

    @Override
    public void afterCompletion(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("============清理============");
    }
}
