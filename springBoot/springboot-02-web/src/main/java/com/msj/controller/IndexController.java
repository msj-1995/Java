package com.msj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
public class IndexController {
    //访问 / 和 /index会转发到首页
    //@RequestMapping({"/","/index"})
    public String index(){
        return "index";
    }
}
