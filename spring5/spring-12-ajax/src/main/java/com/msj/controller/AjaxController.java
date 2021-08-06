package com.msj.controller;

import com.msj.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@RestController会自动返回一个字符串（直接返回字符串，不会跳转页面）
@RestController
public class AjaxController {
    @RequestMapping("/t1")
    public String test(){
        //直接放回一个字符hello串到前端显示
        return "hello";
    }

    /*可以使用Model，ModelAndView,ModelMap等封装数据放回到前端，
    也可以使用HttpServletResponse来返回数据，这里就是用HttpServletResponse*/
    @RequestMapping("/a1")
    public void a1(String name,HttpServletResponse response) throws IOException {
        System.out.println("a1:username=>" + name);
        if("kuangshen".equals(name)){
            response.getWriter().print("true");
        }else{
            response.getWriter().print("false");
        }
    }

    @RequestMapping("/a2")
    public List<User> a2(){
        List<User> userList = new ArrayList<User>();
        //添加模拟数据
        userList.add(new User("kuangshen",1,"男"));
        userList.add(new User("msj",18,"男"));
        userList.add(new User("马",17,"女"));

        return userList;
    }

    @RequestMapping("/a3")
    public String checkUNameAndPwd(String name,String pwd){
        String msg = "";
        if(name!=null){
            //这里是模拟数据，项目中应该在数据库中查询
            if("admin".equals(name)){
                msg = "ok";
            }else{
                msg = "用户名有误";
            }
        }
        if(pwd!=null){
            if("123456".equals(pwd)){
                msg = "ok";
            }else{
                msg = "密码有误";
            }
        }
        return msg;
    }
}
