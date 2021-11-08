package com.msj;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestTX {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.flushDB();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world");
        jsonObject.put("name", "msj");
        String result = jsonObject.toJSONString();

        //开启事务
        Transaction multi = jedis.multi();
        //监视result
        jedis.watch(result);

        //事务操作
        try {
            multi.set("user1", result);
            multi.set("user2", result);
            multi.exec();
        } catch (Exception e) {
            //如果失败，就放弃事务
            multi.discard();
            e.printStackTrace();
        } finally {
            System.out.println(jedis.get("user1"));
            System.out.println(jedis.get("user2"));
            jedis.close();
        }


        jedis.close();
    }
}
