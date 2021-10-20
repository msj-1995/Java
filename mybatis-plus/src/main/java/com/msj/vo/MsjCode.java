package com.msj.vo;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

//代码自动生成器
public class MsjCode {
    public static void main(String[] args) {
        //需要构建一个代码自动生成器对象
        AutoGenerator autoGenerator = new AutoGenerator();
        //配置策略

        //1.全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        //代码的生成目录:获取项目的当前目录：E:\workspace\IDEA\SpringBoot\StudyE:\workspace\IDEA\SpringBoot\Study
        String projectPath = System.getProperty("user.dir");
        //System.out.println(projectPath);
        //设置代码保存路径
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        //添加生成代码的作者
        globalConfig.setAuthor("孟顺建");
        //生成代码后是否打开资源管理器（即文件管理器):这里设置不打开
        globalConfig.setOpen(false);
        //是否覆盖原来的代码
        globalConfig.setFileOverride(false);
        //去除service层的I前缀:如果不去除的或默认是xxxxIService
        globalConfig.setServiceName("%sService");
        //主键生成策略：默认是使用mybatis-plus的配置
        globalConfig.setIdType(IdType.NONE);
        //设置日期类型：使用DateType
        globalConfig.setDateType(DateType.ONLY_DATE);
        //设置自动配置swagger文档
        globalConfig.setSwagger2(true);
        //把全局配置放到代码自动生成器对象中
        autoGenerator.setGlobalConfig(globalConfig);

        //2.数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/mybatis_plus?useSSL=true&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("1234");
        //配置自己的数据库类型：我用的是Mysql
        dataSourceConfig.setDbType(DbType.MYSQL);
        //放到代码自动构建器中
        autoGenerator.setDataSource(dataSourceConfig);

        //3.包的配置
        PackageConfig packageConfig = new PackageConfig();
        //设置模型名
        packageConfig.setModuleName("blog");
        packageConfig.setParent("com.msj");
        packageConfig.setEntity("entity");
        packageConfig.setMapper("mapper");
        packageConfig.setService("service");
        packageConfig.setController("controller");
        autoGenerator.setPackageInfo(packageConfig);

        //4.策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        //命名规则：下划线转驼峰命名
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategyConfig.setSuperEntityClass("你自己的父类实体，没有就不用设置");
        //设置要映射的表名：即要生成代码的数据库表：可变长参数，可设置多个
        strategyConfig.setInclude("user");
        //是否使用Lombok
        strategyConfig.setEntityLombokModel(true);
        //逻辑删除
        strategyConfig.setLogicDeleteFieldName("deleted");
        //要配置自动填充:插入是填充
        TableFill gmt_create = new TableFill("create_time", FieldFill.INSERT);
        TableFill gmt_update = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        //添加到生成策略中
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(gmt_create);
        tableFills.add(gmt_update);
        strategyConfig.setTableFillList(tableFills);
        //乐观锁的配置
        strategyConfig.setVersionFieldName("version");
        //开启restful风格的驼峰命名
        strategyConfig.setRestControllerStyle(true);
        //设置浏览器url映射规则为下划线风格：localhost:8080/hello,携带参数id为2时：localhost:8080/hello_id_2
        autoGenerator.setStrategy(strategyConfig);

        //执行代码构建器
        autoGenerator.execute();

    }

}
