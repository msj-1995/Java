package com.msj.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //登录成功之后，应该有用户的session
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser==null){
            //没有登录
            request.setAttribute("msg","你还没有登录，请登录！");
            request.getRequestDispatcher("/index.html").forward(request,response);
            //没有登录，不放行
            return false;
        }else{
            return true;
        }
    }
}
