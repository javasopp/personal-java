package com.mds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * a 系统启动类
 *
 * @author sopp
 */
@MapperScan("com.mds.*.mapper")
@SpringBootApplication
public class ProjectAApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectAApplication.class, args);
    }

}
