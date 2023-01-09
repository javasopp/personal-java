package com.sopp.all.config.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: WebConfiguration
 * @Description: mvc的配置类：配置全局的跨域请求
 * @Author: Sopp
 * @Date: 2019/4/19 9:21
 **/
@Configuration
@Slf4j
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * 功能描述: 配置全局的跨域请求。
     * @param registry
     * @return void
     * @auther Sopp
     * @date: 2019/4/22 9:12
     * @throws
     */
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                //所有域都通过
                .allowedOrigins("*")
                //适配以下请求
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                //最大时间
                .maxAge(10000)
                //允许凭据
                .allowCredentials(true);
    }


}