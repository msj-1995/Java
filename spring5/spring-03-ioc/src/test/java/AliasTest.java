import com.msj.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AliasTest {
    public static void main(String[] args) {
        //spring管理对象的创建
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //获得对象
        User user = (User)context.getBean("user");
        User user1 = (User)context.getBean("newUser");
        user.show();
        user1.show();
    }
}
