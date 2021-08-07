package com.msj.springcloud.mapper;

// 注意：这里的Dept是通过pom导入的，因为它属于另一个模块
import com.msj.springcloud.api.pojo.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

// 表示这个一个Mapper接口
@Mapper
// 让spring托管
@Repository
public interface DeptMapper {
    public boolean addDept(Dept dept);

    public Dept queryById(Long id);

    public List<Dept> queryAll();
}
