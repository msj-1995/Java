import com.msj.mapper.UserMapper;
import com.msj.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyTest {
    @Test
    public void test1() throws IOException {
        String resources = "mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(resources);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getUser();
        for (User user : userList) {
            System.out.println(user);
        }
    }

    //spring-mybatis测试
    @Test
    public void testSpringMybatis(){
        //使用applicationContext类获得配置文件
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-dao.xml");
        /*执行UserMapper中的方法,下面的代码也可以写为：
        UserMapper userMapper = context.getBean("userMapperImpl",UserMapper.class);
         */
        UserMapper userMapper = (UserMapper)context.getBean("userMapperImpl");
        for (User user : userMapper.getUser()) {
            System.out.println(user);
        }
    }

    @Test
    public void testSpringSqSessionDaoSupport(){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-dao.xml");
        UserMapper mapper = context.getBean("userMapperImpl2",UserMapper.class);
        for (User user : mapper.getUser()) {
            System.out.println(user);
        }
    }
}
