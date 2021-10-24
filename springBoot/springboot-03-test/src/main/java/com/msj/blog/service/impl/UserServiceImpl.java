package com.msj.blog.service.impl;

import com.msj.blog.entity.User;
import com.msj.blog.mapper.UserMapper;
import com.msj.blog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 孟顺建
 * @since 2021-05-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
