import com.msj.pojo.Student;
import com.msj.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        Student student = (Student)context.getBean("student");
        System.out.println(student.toString());
    }

    @Test
    public void testP(){
        ApplicationContext context = new ClassPathXmlApplicationContext("userbeans.xml");
        User user = (User)context.getBean("user");
        System.out.println(user.toString());
    }

    @Test
    public void testC(){
        ApplicationContext context = new ClassPathXmlApplicationContext("userbeans.xml");
        User user1 = context.getBean("user2", User.class);
        User user2 = context.getBean("user2", User.class);
        System.out.println(user1==user2);
        System.out.println(user1.hashCode());
        System.out.println(user2.hashCode());
    }
}
