package com.msj.dao;

import com.msj.pojo.Department;
import com.msj.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//让员工类给spring托管(被托管之后就可以使用@Autowired注解自动装配了），下面的注解与@Component和@Service注解类似
@Repository
//员工Dao
public class EmployeeDao {
    //模拟员工的数据
    private static Map<Integer,Employee> employees = null;
    //员工有所属的部门
    @Autowired
    private DepartmentDao departmentDao;

    static{
        employees = new HashMap<Integer,Employee>();
        employees.put(1001,new Employee(1001,"小王","123456@qq.com",1,new Department(101,"教学部")));
        employees.put(1002,new Employee(1002,"小兰","12256@qq.com",0,new Department(101,"教学部")));
        employees.put(1003,new Employee(1003,"小红","33678@qq.com",1,new Department(102,"管理部")));
        employees.put(1004,new Employee(1004,"小酷","442315@qq.com",0,new Department(103,"教研部")));
        employees.put(1005,new Employee(1005,"小酷","3309051@qq.com",1,new Department(104,"运营部")));
        employees.put(1006,new Employee(1006,"小酷","2213145@qq.com",1,new Department(105,"财政部")));
    }
    //主键自增
    //增加一个员工
    private static Integer initId = 1006;
    public void addEmployee(Employee employee){
        if(employee.getId()==null){
            //如果员工id为空，id在initId的基础上自增1
            employee.setId(++initId);
        }
        //设置员工的部门
        employee.setDepartment(departmentDao.getDepartmentById(employee.getDepartment().getId()));
        //添加员工
        employees.put(employee.getId(),employee);
    }

    //查询全部员工
    public Collection<Employee> getAllEmployee(){
        return employees.values();
    }

    //通过id查询员工
    public Employee getEmployeeById(Integer id){
        return employees.get(id);
    }

    //通过id删除员工
    public void deleteEmployeeById(Integer id){
        employees.remove(id);
    }
}
