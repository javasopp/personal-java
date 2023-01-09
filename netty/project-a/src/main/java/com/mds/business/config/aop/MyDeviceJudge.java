package com.mds.business.config.aop;

import com.mds.business.common.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * 提前判断设备是否在线的aop
 * @author sopp
 * @version 1.0
 * @date 2021/6/28 17:05
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MyDeviceJudge {

    private final RedisTemplate redisTemplate;

    @Pointcut("@annotation(com.mds.business.common.annotation.MyJudge)")
    public void annotationPointcut() {

    }


    @Before("annotationPointcut()")
    public void beforePointcut() throws MyException {
        // 此处进入到方法前  可以实现一些业务逻辑
    }
}
