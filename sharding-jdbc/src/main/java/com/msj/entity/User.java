package com.msj.entity;

import lombok.Data;

@Data
public class User {
    //主键
    private Integer id;
    //昵称
    private String nickname;
    //密码
    private String password;
    //性别
    private Integer sex;
    //生日
    private String birthday;
}
