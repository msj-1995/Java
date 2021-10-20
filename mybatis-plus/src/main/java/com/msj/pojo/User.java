package com.msj.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    //逻辑删除
    @TableLogic
    private int deleted;
    //对应数据库中的主键（uuid,自增id，雪花算法，redis,zookeeper)
    private Long id;
    private String name;
    private int age;
    private String email;
    //插入时更新
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //插入和更新时都更新
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //@Version:mybatis下的注解，乐观锁Version注解
    @Version
    private Integer version;
}
