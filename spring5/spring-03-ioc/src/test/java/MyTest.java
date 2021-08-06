import com.msj.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        //传统
        //User user = new User();

        //利用spring来创建管理对象的创建
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //get我们需要的bean
        User user = (User)context.getBean("user");
        User user1 = (User)context.getBean("user");
        //获取对象
        user.show();
        System.out.println(user==user1);
    }
}
