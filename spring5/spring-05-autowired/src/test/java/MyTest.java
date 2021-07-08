import com.msj.pojo.People;
import com.msj.pojo.People1;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    @Test
    public void test1(){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        People people = context.getBean("people", People.class);
        people.getCat().shout();
        people.getDog().shot();
    }

    @Test
    public void test2(){
        ApplicationContext context = new ClassPathXmlApplicationContext("beansByType.xml");
        People people = context.getBean("people", People.class);
        people.getCat().shout();
        people.getDog().shot();
    }

    @Test
    public void testAutowire(){
        ApplicationContext context = new ClassPathXmlApplicationContext("22.xml");
        People people = context.getBean("people", People.class);
        people.getCat().shout();
        people.getDog().shot();
    }

    @Test
    public void testResource(){
        ApplicationContext context = new ClassPathXmlApplicationContext("resourceAnnotation.xml");
        People1 people = context.getBean("people", People1.class);
        people.getCat().shout();
        people.getDog().shot();
    }
}
