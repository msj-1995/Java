import com.msj.config.MyConfig;
import com.msj.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        //如果完全hi用了配置类的方式去做，我们就只能通过AnnotationConfig上下文来获取容器，通过配置类的class对象加载！
        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        //方法名就是bean的名字（方法是MyConfig中的方法）
        User user = (User)context.getBean("getUser");
        System.out.println(user.getName());
    }
}
