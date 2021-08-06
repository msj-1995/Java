package com.msj.springcloud.api.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
// 支持链式写法注解
@Accessors(chain = true)
public class Dept implements Serializable {
    // Dept实体类
    private Long deptno; // 主键
    private String dname;
    // 这个字段存放使用那个数据库字段的字段， 微服务->一个服务对应一个数据库,同一个信息可能存在不同的数据库
    private String db_source;

    public Dept(String dname) {
        this.dname = dname;
    }

    /**
     * 链式写法与非链式写法
     * 非链式写法：
     * Dept dept = new Dept();
     * dept.setDeptno();
     * dept.setDname()
     *
     * 使用链式写法：
     * dept.setDeptno().setDname().setDb_source()
     */
}
