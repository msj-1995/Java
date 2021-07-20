package com.msj.mapper;

import com.msj.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    public List<User> getUser();

    //添加一个用户
    public int addUser(User user);

    //删除一个用户
    public int deleteUser(@Param("id") int id);
}
