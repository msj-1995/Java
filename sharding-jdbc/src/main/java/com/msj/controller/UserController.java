package com.msj.controller;

import com.msj.entity.User;
import com.msj.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserMapper userMapper;
    @GetMapping("/save")
    public String insert(){
        User user = new User();
        user.setNickname("张三" + new Random().nextInt());
        user.setPassword("123456789");
        user.setSex(1);
        user.setBirthday("1195-12-03");
        userMapper.addUser(user);
        return "success";
    }

    @GetMapping("/listuser")
    public List<User> userList(){
        return userMapper.findUsers();
    }
}
