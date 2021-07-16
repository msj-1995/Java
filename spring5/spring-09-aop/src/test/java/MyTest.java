import com.msj.service.UserService;
import com.msj.service.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        //动态代理代理的是接口，所以下面应该使用接口，而不是实现类
        UserService userService = context.getBean("userService", UserService.class);
        userService.add();
    }
}
