package com.mds.business.config.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: GeneratorEntityNew
 * @Description: mybatis-plus逆向工程。
 * @Author: Sopp
 * @Date: 2019/4/22 14:20
 **/
public class GeneratorEntityNew {

    /**
     * 代码生成的根路径
     */
    private static final String BASEDIR = "/Users/sopp/Desktop/code";

    /**
     * 根路径
     */
    private static final String PARENT_PACKAGE = "com.mds.auth";

    public static void main(String[] args) {

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //项目路径，这里还是设置为D盘，否则生成到项目目录，替换掉之前的代码。
        gc.setOutputDir(BASEDIR);
        //设置作者
        gc.setAuthor("Sopp");
        //是否立刻打开
        gc.setOpen(true);
        //设置时间格式
        gc.setDateType(DateType.ONLY_DATE);
        //设置主键类型
        gc.setIdType(IdType.AUTO);
        //
        gc.setEntityName("%s").setControllerName("%sController").setServiceName("%sService").setServiceImplName("%sServiceImpl").setMapperName("%sMapper");
        //设置生成代码的名字
        mpg.setGlobalConfig(gc);


        //数据源配置,包括了数据库类型，用户名密码，链接地址。
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver").setDbType(DbType.MYSQL);
        dataSourceConfig.setUsername("root").setPassword("root").setUrl("jdbc:mysql://localhost:3307/auth?useSSL=false&serverTimezone=UTC");
        mpg.setDataSource(dataSourceConfig);


        //包的配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(PARENT_PACKAGE);
        mpg.setPackageInfo(packageConfig);


        StrategyConfig strategyConfig = new StrategyConfig();

        //设置表名大小写转换
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);

        //设置生成的表名，这里使用exClude来进行排除，写入null，表示生成所有表。
        strategyConfig.setExclude(null);
        //设置自定义的service层继承代码，这里使用我定义的service和impl来进行继承---Sopp
        //实体类是否带有lombok风格
        strategyConfig.setEntityLombokModel(true);
        //rest风格controller
        strategyConfig.setRestControllerStyle(true);
        //允许字段自动注解
        strategyConfig.setEntityTableFieldAnnotationEnable(true);
        //填充字段配置
        List<TableFill> list = new ArrayList<>();
        strategyConfig.setTableFillList(list);

        TemplateConfig templateConfig = new TemplateConfig();
        mpg.setTemplate(templateConfig);

        mpg.setStrategy(strategyConfig);

        mpg.execute();
    }
}