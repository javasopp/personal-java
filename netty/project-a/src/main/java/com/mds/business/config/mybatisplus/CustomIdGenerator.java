package com.mds.business.config.mybatisplus;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author sopp
 * @description 生成全局id的方法
 */
@Slf4j
@Component
public class CustomIdGenerator implements IdentifierGenerator {


    @Override
    public Long nextId(Object entity) {
        //可以将当前传入的class全类名来作为bizKey,或者提取参数来生成bizKey进行分布式Id调用生成.
        String bizKey = entity.getClass().getName();
        //根据bizKey调用分布式ID生成
        long id = IdUtil.getSnowflake(1, 1).nextId();
        //返回生成的id值即可.
        return id;
    }
}
