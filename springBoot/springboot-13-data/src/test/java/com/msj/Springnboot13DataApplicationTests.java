package com.msj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class Springnboot13DataApplicationTests {

    /*springboot中，只要我们导入了jdbc和配置数据库连接，他就会为我们自动生成一些数据源的对象,并可以自动注入过来*/
    @Autowired
    DataSource dataSource;
    @Test
    void contextLoads() throws SQLException {
        //查看以下默认的数据源：class com.zaxxer.hikari.HikariDataSource
        System.out.println(dataSource.getClass());

        //获得数据库连接
        Connection connection = dataSource.getConnection();
        //输出看一下
        System.out.println(connection);
        connection.close();
    }

}
