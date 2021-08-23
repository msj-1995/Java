package com.msj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JDBCController {

    //spring中的 xxxTemplate：spring中已经配置号的bean，拿来即用
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/user")
    //查询数据库所有的信息并显示到页面上
    //没有实体类：数据库中的东西如何获取（使用Map封装）
    public List<Map<String,Object>> userList(){
        String sql = "select * from user";
        List<Map<String,Object>> list_maps = jdbcTemplate.queryForList(sql);
        return list_maps;
    }

    //增加用户
    @GetMapping("/addUser")
    public String addUser(){
        String sql="insert into mybatis.user(name,pwd) values('小明','123456456')";
        jdbcTemplate.update(sql);
        //这里事务spring帮我们做了，我们不用在提交事务了
        return "updateOk";
    }

    //更新用户
    @GetMapping("/updateUser/{id}")
    public String updateUser(@PathVariable("id")int id){
        String sql = "update mybatis.user set name=?,pwd=? where id=" + id;
        //封装数据
        Object[] objects = new Object[2];
        objects[0] = "小花";
        objects[1] = "1314159";
        jdbcTemplate.update(sql,objects);
        return "updateOk";
    }

    //删除用户
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id){
        String sql = "delete from mybatis.user where id=?";
        jdbcTemplate.update(sql,id);
        return "delete OK";
    }

}
