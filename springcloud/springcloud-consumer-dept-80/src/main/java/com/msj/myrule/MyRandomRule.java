package com.msj.myrule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MyRandomRule extends AbstractLoadBalancerRule {
    // 每个服务访问5次，换下一个任务(我们这里有三个任务)

    // total=0，默认0，如果为5，指向下一个服务节点

    // index=0,默认0,如果total=5,index++,如果index>=0,index置零

    private int total = 0; // 被调用的次数

    public MyRandomRule() {
    }
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        } else {
            Server server = null;

            while(server == null) {
                if (Thread.interrupted()) {
                    return null;
                }
                // 获得活着的服务
                List<Server> upList = lb.getReachableServers();
                // 获得全部服务
                List<Server> allList = lb.getAllServers();
                int serverCount = allList.size();
                if (serverCount == 0) {
                    return null;
                }

                // 生成区间随机数
                int index = this.chooseRandomInt(serverCount);
                // 从活着的服务中，随机获取一个服务
                server = (Server)upList.get(index);
                // 如果没有服务，线程礼让后继续
                if (server == null) {
                    Thread.yield();
                } else {
                    if (server.isAlive()) {
                        return server;
                    }

                    server = null;
                    Thread.yield();
                }
            }

            return server;
        }
    }

    protected int chooseRandomInt(int serverCount) {
        return ThreadLocalRandom.current().nextInt(serverCount);
    }

    public Server choose(Object key) {
        return this.choose(this.getLoadBalancer(), key);
    }

    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }
}
