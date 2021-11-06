package com.msj;

import redis.clients.jedis.Jedis;

import java.util.Set;

public class TestPing {
    public static void main(String[] args) {
        //1、new jedis对象即可（里面后很多重载方法，也可以使用ssh安全连接）
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //2.jedis所有的命令就是我们之前学习的所有指令,返回pong则证明连接成功
        System.out.println(jedis.ping());

        System.out.println("清空数据" + jedis.flushDB());
        System.out.println("判断某个键是否存在" + jedis.exists("username"));
        System.out.println("新增<username,msj>键值对" + jedis.set("username", "msj"));
        System.out.println("新增<password,123456>键值对" + jedis.set("password", "123456"));
        System.out.println("系统中所有的键如下");
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);
        System.out.println("取出key：password：" + jedis.del("password"));
        System.out.println("判断password是否存在：" + jedis.exists("password"));
        System.out.println("查看username的类型：" + jedis.type("username"));
        System.out.println("随机返回一个key空间的一个：" + jedis.randomKey());
        System.out.println("重命名key：" + jedis.rename("username", "name"));
        System.out.println("取出修改后的name：" + jedis.get("name"));
        System.out.println("按索引查询：" + jedis.select(0));
        System.out.println("删除当前数据库中的所有key:" + jedis.flushDB());
        System.out.println("返回当前数据库中key的数目：" + jedis.dbSize());
        System.out.println("删除所有数据库中的所有key：" + jedis.flushAll());

    }
}
