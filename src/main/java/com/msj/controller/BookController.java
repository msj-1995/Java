package com.msj.controller;

import com.msj.pojo.Books;
import com.msj.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    //controller 调 service层
    //Autowired根据类型自动装配
    @Autowired
    //根据名字自动装配
    @Qualifier("bookServiceImpl")
    private BookService bookService;

    //查询所有的书籍并返回到一个书籍展示页面
    @RequestMapping(value="/allBook")
    public String bookList(Model model){
        List<Books> books = bookService.queryAllBook();
        model.addAttribute("bookList",books);
        return "allBook";
    }

    //跳转到增加书籍页面
    @RequestMapping("/toAddBook")
    public String toAddBook(){
        return "addBook";
    }

    //添加书籍的请求
    @RequestMapping("/addBook")
    public String addBook(Books book){
        System.out.println("addBook=>" + book);
        bookService.addBook(book);
        //重定向到@RequestMapping("/allBook")请求
        return "redirect:/book/allBook";
    }

    //跳转到修改页面
    @RequestMapping("/toUpdate")
    public String toUpdatePaper(int id,Model model){
        Books book = bookService.queryBookById(id);
        model.addAttribute("book",book);
        //需要去jsp目录下新建一个updateBook的jsp页面
        return "updateBook";
    }

    //修改书籍
    @RequestMapping("/updateBook")
    public String updateBook(Books books){
        System.out.println("updateBooK=>" + books);
        int row = bookService.updateBook(books);
        if(row>0){
            System.out.println("修改成功");
            return "redirect:/book/allBook";
        }
        else{
            System.out.println("修改失败");
            return "redirect:/book/allBook";
        }
    }

    //删除书籍
    @RequestMapping("/deleteBook/{bookId}")
    public String deleteBook(@PathVariable("bookId") int id){
        bookService.deleteBookById(id);
        return "redirect:/book/allBook";
    }

    //查询书籍
    @RequestMapping("/queryBook")
    public String queryBook(String queryBookName,Model model){
        Books books = bookService.queryBookByName(queryBookName);
        //将查询出来的书籍封装到list集合中传递给前端
        List<Books> list = new ArrayList<Books>();
        list.add(books);
        if(books==null){
            list = bookService.queryAllBook();
            model.addAttribute("error","没有查到相关数据");
        }
        model.addAttribute("bookList",list);
        return "allBook";
    }
}
