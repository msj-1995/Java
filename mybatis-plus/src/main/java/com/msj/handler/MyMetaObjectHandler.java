package com.msj.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

//@XSlf4j，加sl4j日志
@Slf4j
//一定不要忘记把处理器添加到IOC容器中
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    //插入时数据填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("插入填充......");
        //setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject)
        //fieldName 要修改的字段名  fieldVal：要修改为的值  metaObject：要给那个字段处理
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }

    //更新时数据填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("更新填充.....");
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}
