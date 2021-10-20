package com.msj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msj.pojo.User;
import org.springframework.stereotype.Repository;

//在对应的Mapper上面继承基本的类BaseMapper
@Repository  //注册为spring组件  代表持久层
public interface UserMapper extends BaseMapper<User> {
    //所有的CRUD操作都已经编写完成了
    //你不需要像以前的配置，配置一大推文件了
}
