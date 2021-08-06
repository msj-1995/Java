package com.msj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class LoginController {
    @RequestMapping("/login")
    public String login(String username, String password, HttpSession session, Model model){
        System.out.println("LonginController=>model" + model);
        System.out.println("LonginController=>login()");
        System.out.println("login=>" + username);
        model.addAttribute("username",username);
        //把用户的信息存在session中
        session.setAttribute("userLoginInfo",username);
        return "main";
    }

    //进入首页的请求
    @RequestMapping("/main")
    public String main(){
        System.out.println("LonginController=>main()");
        return "main";
    }

    //进入登录页面
    @RequestMapping("/goLogin")
    public String goLogin(){
        System.out.println("LonginController=>goLogin()");
        return "login";
    }

    //注销
    @RequestMapping("/goOut")
    public String goOut(HttpSession session){
        //移除session
        session.removeAttribute("userLoginInfo");
        return "main";
    }
}
