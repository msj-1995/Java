import com.msj.dao.BookMapper;
import com.msj.pojo.Books;
import com.msj.service.BookService;
import com.msj.service.BookServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import utils.MybatisUtils;

public class MyTest {
    @Test
    public void test(){
        SqlSession sqlSession = MybatisUtils.getSession();
        BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
        BookServiceImpl bookService = new BookServiceImpl();
        bookService.setBookMapper(bookMapper);
        Books books = new Books(4,"Vue",6,"从来到去");
        int row = bookService.addBook(books);
        if(row>0){
            System.out.println("插入成功！");
        }
    }

    @Test
    public void test1(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookService bookService = (BookService)applicationContext.getBean("bookServiceImpl");
        for (Books books : bookService.queryAllBook()) {
            System.out.println(books);
        }
    }
}
