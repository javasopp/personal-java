package com.sopp.all.config.aop;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: LogAspect
 * @Description: aop info
 * @Author: Sopp
 * @Date: 2019/7/15 9:36
 **/
@Aspect
@Component
@Order(9)
@Slf4j
public class LogAspect {

    /**
     * the point cut to into the function
     */
    @Pointcut("@annotation(com.sopp.all.common.annotation.MyLog)")
    public void logPointCut() {

    }

    /**
     * Description: the aspect info
     *
     * @param joinPoint
     * @return void
     * @throws
     * @auther Sopp
     * @date: 2019/7/15 10:06
     */
    @AfterReturning("logPointCut()")
    public void saveSysLog(JoinPoint joinPoint) {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //为了获取注解的参数
//        MyLog myLog = method.getAnnotation(MyLog.class);

        //请求的参数
        Object[] args = joinPoint.getArgs();
        //将参数所在的数组转换成json
//        String params = JSON.toJSONString(args);
        Map<String, Object> map = new HashMap<>(16);
        log.trace(JSON.toJSONString(map));
    }
}