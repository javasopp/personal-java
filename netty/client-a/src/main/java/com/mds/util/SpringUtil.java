package com.mds.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/11/10 15:12
 */
@Slf4j
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;


    public SpringUtil() {

    }


    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        if (applicationContext == null) {
            applicationContext = arg0;
        }
    }


    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    public void setAppCtx(ApplicationContext webAppCtx) {
        if (webAppCtx != null) {
            applicationContext = webAppCtx;
        }
    }


    /**
     * 拿到ApplicationContext对象实例后就可以手动获取Bean的注入实例对象
     */
    public <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public <T> T getBean(String name, Class<T> clazz) throws ClassNotFoundException {
        return getApplicationContext().getBean(name, clazz);
    }

    public final Object getBean(String beanName) {
        return getApplicationContext().getBean(beanName);
    }


    public final Object getBean(String beanName, String className) throws ClassNotFoundException {
        Class<?> clz = Class.forName(className);
        return getApplicationContext().getBean(beanName, clz.getClass());
    }

    public boolean containsBean(String name) {
        return getApplicationContext().containsBean(name);
    }

    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return getApplicationContext().isSingleton(name);
    }


    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getApplicationContext().getType(name);
    }


    public String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return getApplicationContext().getAliases(name);
    }

}
