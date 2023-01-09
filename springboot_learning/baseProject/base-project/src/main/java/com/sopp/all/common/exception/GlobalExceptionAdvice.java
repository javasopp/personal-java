package com.sopp.all.common.exception;


import com.sopp.all.common.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: GlobalExceptionAdvice
 * @Description: Global exception
 * @Author: Sopp
 * @Date: 2019/4/19 9:59
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 提前验证异常处理
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = ErrorException.class)
    public Message preJudgeParam(HttpServletRequest request, Exception exception) {
        log.info("request url：" + request.getRequestURI());
        exception.printStackTrace();
        return Message.error(exception.getMessage());
    }


    /**
     * 全局异常处理
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Message error(HttpServletRequest request, Exception exception) {
        log.info("request url：" + request.getRequestURI());
        log.error(exception.getMessage());
        return Message.error(exception.getMessage());
    }
}
