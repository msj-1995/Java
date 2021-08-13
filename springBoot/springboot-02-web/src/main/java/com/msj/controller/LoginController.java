package com.msj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String pwd, Model model, HttpSession session){
        //具体业务
        if(!StringUtils.isEmpty(username)&&"123456".equals(pwd)){
            //添加用户session
            session.setAttribute("loginUser",username);
            //登录成功重定向到main.html
            return "redirect:/main.html";
        }else{
            //告诉用户，登录失败
            model.addAttribute("msg","用户名或者密码错误");
            return "index";
        }
    }
}
