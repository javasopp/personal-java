package com.sopp.all;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类。
 * @author Sopp
 * @date 2019-7-3 14:28:29
 */
@EnableScheduling
@MapperScan("com.sopp.all.mapper")
@SpringBootApplication
public class SoppAllApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SoppAllApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SoppAllApplication.class);
    }
}
