package com.msj;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msj.mapper.UserMapper;
import com.msj.pojo.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.input.LineSeparatorDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class WrapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads(){
        //查询name不为空的用户，并且邮箱也不为空，年龄大于12
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        //wrapper支持链式编程
        userQueryWrapper.isNotNull("name")
            .isNotNull("email")
            .ge("age",12);
        userMapper.selectList(userQueryWrapper).forEach(System.out::println);
    }

    //测试二
    @Test
    void test2(){
        //查询name为孟顺建的:只查询一个：使用selectOne
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name","孟顺建");
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

    //测试三：between...and
    @Test
    void test3(){
        //年龄在20-30之间
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("age",20,30); //区间
        Integer count = userMapper.selectCount(queryWrapper);
        //输出查询到的人数
        System.out.println(count);
    }

    //测试四
    @Test
    void test4(){
        //模糊查询:name中不包含e的
        //like中左和右：%xxx% 就是%的位置 e%:右边，即左边有个e，右边通配
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.notLike("name","e")
                //以2开头
                .likeRight("email","2");
        //使用map接收
        List<Map<String,Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);
    }

    //测试五：连接查询
    @Test
    void test(){
        //id是在子查询中查出来的
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id","select id from user where id<4");
        List<Object> objects = userMapper.selectObjs(queryWrapper);
        //objects也可以使用forEach打印
        objects.forEach(System.out::println);
    }

    //测试六
    @Test
    void test6(){
        //通过id进行排序
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }
}
