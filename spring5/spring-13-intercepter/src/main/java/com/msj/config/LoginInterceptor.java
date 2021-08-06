package com.msj.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //放行判断:判断什么情况下登录
        if(session.getAttribute("userLoginInfo")!=null){
            System.out.println("LoginInterceptor=>userLoginInof");
            //已经登录，放行
            return true;
        }
        //去登录页面也要放行 请求中包含goLogin则放行
        if(request.getRequestURI().contains("goLogin")){
            System.out.println("LoginInterceptor=>goLogin");
            return true;
        }
        //在登录页面，此时要放行
        if(request.getRequestURI().contains("login")){
            System.out.println("LoginInterceptor=>login");
            return true;
        }

        //其他情况不放行 转发到其他页面(登录页面
        //判断什么情况下没有登录
        System.out.println("LogonInterceptor=>方法执行了");
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
        return false;
    }
}
