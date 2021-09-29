package com.msj;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msj.mapper.UserMapper;
import com.msj.pojo.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class MybatisPlusApplicationTests {
    //继承了BaseMapper,所有的方法都来自于BaseMapper(如果BaseMapper中没有就需要自己编写）
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        /*
        * selectList()的参数是一个Wrapper,条件构造器，这里我们先不用，直接填null
        * */
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testInsert(){
        User user = new User();
        user.setName("狂神说2");
        user.setAge(18);
        user.setId(12L);
        user.setEmail("200012354@qq.com");
        int result = userMapper.insert(user);
        System.out.println(result);
        System.out.println(user);
    }

    @Test
    public void testUpdate(){
        User user = new User();
        user.setId(3L);
        user.setName("孟顺建");
        user.setAge(21);
        user.setEmail("123456@qq.com");
        //注意：updateById,但是参数是一个对象
        int i = userMapper.updateById(user);
        System.out.println(i);
    }

    //测试乐观锁：成功
    @Test
    public void testOptimisticLocker(){
        //查询用户信息
        User user = userMapper.selectById(1L);
        //修改用户信息
        user.setName("孟顺建");
        user.setEmail("456789@qq.com");
        //执行更新操作：单线程情况下，成功
        userMapper.updateById(user);
    }

    //多线程下，测试失败
    @Test
    public void testOptimisticLocker2(){
        //线程一
        //查询用户信息
        User user = userMapper.selectById(1L);
        //修改用户信息
        user.setName("孟顺建1");
        user.setEmail("456789@qq.com");

        //模拟另一个线程执行了插队操作
        User user2 = userMapper.selectById(1L);
        //修改用户信息
        user2.setName("孟顺建2");
        user2.setEmail("456789@qq.com");

        userMapper.updateById(user2);
        //自旋锁尝试多次提交
        userMapper.updateById(user);
        //如果没有乐观锁就会user就会覆盖user2的值，但是并没有
    }

    //测试查询
    @Test
    public void testSelectById(){
        User user = userMapper.selectById(3L);
        System.out.println(user);
    }
    //批量查询
    @Test
    public void testBatch(){
        List<User> userList = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        for (User user : userList) {
            System.out.println(user);
        }

    }

    //条件查询
    @Test
    public void testSelectByBatchIds(){
        //自定义查询
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "孟顺建");
        List<User> user = userMapper.selectByMap(map);
        for (User user1 : user) {
            System.out.println(user1);
        }
    }

    //分页查询测试
    @Test
    public void testPage(){
        //Page: 参数一：当前页   参数二：页面大小
        Page<User> page = new Page<>(1,5);
        userMapper.selectPage(page, null);
        page.getRecords().forEach(System.out::println);
    }

    //根据id删除用户
    @Test
    public void deleteById(){
        userMapper.deleteById(3);
    }

    //根据id批量删除
    @Test
    public void deleteBatch(){
        userMapper.deleteBatchIds(Arrays.asList(2L,3L));
    }

    //条件删除
    @Test
    public void testDeleteByMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","狂神说java");
        userMapper.deleteByMap(map);
    }
}
