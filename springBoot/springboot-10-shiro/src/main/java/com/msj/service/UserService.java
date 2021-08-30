package com.msj.service;

import com.msj.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserService {
    User queryUserByName(String name);
}
