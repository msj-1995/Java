package com.msj.mapper;

import com.msj.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper 表示这是一个mybatis的mapper类 也可以在springboot的主启动类上写@MapperScan注解(这两个注解使用一个就可以了)
@Mapper
//注册为spring的一个组件（即表示被spring接管）
@Repository
public interface UserMapper {
    List<User> queryUserList();

    User queryUserById(int id);

    int addUser(User user);

    int updateUser(User user);

    int deleteUserById(int id);
}
