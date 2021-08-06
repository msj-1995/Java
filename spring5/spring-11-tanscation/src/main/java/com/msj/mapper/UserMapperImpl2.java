package com.msj.mapper;

import com.msj.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;

public class UserMapperImpl2 extends SqlSessionDaoSupport implements UserMapper {
    @Override
    public List<User> getUser() {
        /*为了让添加用户、删除用户和该方法变成一组事务，需要在该方法中加入两外两个方法
        * 即加入addUser和deleteUser这两个方法
        * */
        User user = new User(9,"小兰","123456");

        UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
        mapper.addUser(user);
        mapper.deleteUser(8);

        return getSqlSession().getMapper(UserMapper.class).getUser();
    }

    @Override
    public int addUser(User user) {
        return getSqlSession().getMapper(UserMapper.class).addUser(user);
    }

    @Override
    public int deleteUser(int id) {
        return getSqlSession().getMapper(UserMapper.class).deleteUser(id);
    }
}
