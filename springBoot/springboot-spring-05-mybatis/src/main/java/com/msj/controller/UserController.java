package com.msj.controller;

import com.msj.mapper.UserMapper;
import com.msj.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/queryUserList")
    public List<User> queryUserList(){
        List<User> userList = userMapper.queryUserList();
        for (User user : userList) {
            System.out.println(user);
        }
        return userList;
    }

    @GetMapping("/queryUserById")
    public User queryUserById(){
        User user = userMapper.queryUserById(3);
        System.out.println(user);
        return user;
    }

    @GetMapping("/updateUser")
    public String updateUser(){
        User user = new User(4,"小兰","14171789");
        int row = userMapper.updateUser(user);
        if(row>0){
            return "update Ok";
        }
        else{
            return "update Failed";
        }
    }

    @GetMapping("/deleteUser")
    public String deleteUser(){
        int row = userMapper.deleteUserById(9);
        if(row>0){
            return "delete Ok";
        }
        else{
            return "delete Failed";
        }
    }

    @GetMapping("/addUser")
    public String addUser(){
        User user = new User(9,"小兰","14171789");
        int row = userMapper.addUser(user);
        if(row>0){
            return "insert Ok";
        }
        else{
            return "insert Failed";
        }
    }
}
