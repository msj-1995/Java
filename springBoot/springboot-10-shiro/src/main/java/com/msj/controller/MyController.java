package com.msj.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class MyController {
    @RequestMapping({"/", "/index","index.html"})
    public String toIndex(Model model){
        model.addAttribute("msg","hello shiro");
        return "index";
    }

    @RequestMapping("/user/add")
    public String addUser(){
        return "user/addUser";
    }

    @RequestMapping("/user/update")
    public String updateUser(){
        return "user/updateUser";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(@RequestParam("username") String uname, @RequestParam("password") String pwd, Model model){
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        System.out.println(uname);
        System.out.println(pwd);
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(uname,pwd);
        System.out.println(token);

        //执行登录方法，如果没有异常，就说明ok了
        try{
            subject.login(token);
            return "index";
        }catch(UnknownAccountException e){
            //用户名不存在异常
            model.addAttribute("msg","用户名不存在或错误");
            return "login";
        }catch(IncorrectCredentialsException e){
            //密码错误
            model.addAttribute("msg","密码错误");
            return "login";
        }
    }

    @RequestMapping("/noauth")
    @ResponseBody
    public String unauthorized(){
        return "未经授权无法访问此页面";
    }
}
