package com.msj.controller;

import com.msj.dao.DepartmentDao;
import com.msj.dao.EmployeeDao;
import com.msj.pojo.Department;
import com.msj.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@Controller
public class EmployeeController {
    /*controller层调用service层，但这里为了快速测试，没有写service层，所以直接调用dao层*/
    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    DepartmentDao departmentDao;

    //查询全部员工
    @RequestMapping("/emps")
    public String list(Model model){
        Collection<Employee> employees = employeeDao.getAllEmployee();
        model.addAttribute("employees",employees);
        return "/employee/list";
    }

    @GetMapping("/emp")
    public String toAddEmployeePage(Model model){
        //跳到添加页面的时候需要查出所有的部门信息，不能让前端显示1，2，3，4，5之类的数据
        //查出所有部门的信息
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("departments",departments);
        return "employee/add";
    }

    @PostMapping("/emp")
    public String addEmployee(Employee employee){
        System.out.println("employee=>" + employee);
        //添加操作
        employeeDao.addEmployee(employee);  //调用底层业务方法添加员工信息
        //添加成功，回到/emps去显示列表
        return "redirect:/emps";
    }

    //去员工的修改页面
    @GetMapping("/emp/{id}")
    public String toUpdatePage(@PathVariable("id") Integer employeeId,Model model){
        //查出全部员工信息
        Employee employee = employeeDao.getEmployeeById(employeeId);
        model.addAttribute("emp",employee);
        //查出部门信息
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("departments",departments);
        return "employee/update";
    }

    @PostMapping("/update")
    public String updateEmp(Employee employee){
        employeeDao.addEmployee(employee);
        return "redirect:/emps";
    }

    //删除员工
    @GetMapping("/del/{id}")
    public String delEmp(@PathVariable("id") Integer employeeId){
        employeeDao.deleteEmployeeById(employeeId);
        return "redirect:/emps";
    }

    //注销登录
    @RequestMapping("/user/loginout")
    public String loginout(HttpSession session){
        session.invalidate();
        return "redirect:/index.html";
    }
}
