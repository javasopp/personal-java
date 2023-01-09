package com.sopp.all.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MyMetaObjectHandler
 * @Description: 自动注入
 * @Author: Sopp
 * @Date: 2019/4/19 14:28
 **/
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("插入填充开始~~~~~~~~~~~~~~~~~");
        boolean hasGetter = metaObject.hasGetter("createTime");
        if (hasGetter != false) {
            this.setFieldValByName("createTime", System.currentTimeMillis(), metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("更新填充开始~~~~~~~~~~~~~~~~~");
        boolean hasGetter = metaObject.hasGetter("updateTime");
        if (hasGetter != false) {
            this.setFieldValByName("updateTime", System.currentTimeMillis(), metaObject);
        }
    }
}