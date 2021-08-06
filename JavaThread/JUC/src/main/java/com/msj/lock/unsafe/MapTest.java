package com.msj.lock.unsafe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapTest {
    public static void main(String[] args) {
        //Map是这么用的吗？不是，工作中使用HashMap
        // 默认等价于什么？默认加载因子 0.75，默认等价new HashMap<>(16,0.75);
        //Map<String,String> map = new HashMap<>();
        //Map<String,String> map = Collections.synchronizedMap(new HashMap<>());
        Map<String,String> map = new ConcurrentHashMap<>();
        //加载因子，初始化容量（自己去看）

        for(int i=0;i<=30;i++){
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,5));
                System.out.println(Thread.currentThread().getName() + "-->" + map);
            },String.valueOf(i)).start();
        }
    }
}
