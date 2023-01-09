package com.mds.business.common.exception;


import com.mds.business.common.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: GlobalExceptionAdvice
 * @Description: Global exception
 * @Author: Sopp
 * @Date: 2019/4/19 9:59
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {


    /**
     * 提前验证异常处理
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = ErrorException.class)
    public Message preJudgeParam(HttpServletRequest request, Exception exception) {
        log.info("request url：" + request.getRequestURI());
        return Message.error(exception.getMessage());
    }

    @ExceptionHandler(value = LoginErrorException.class)
    public Message judgeLoginError(HttpServletRequest request, Exception exception) {
        log.info("request url：" + request.getRequestURI());
        return Message.loginError(exception.getMessage());
    }



    /**
     * Functional description: Globally all exceptions are caught, if you don't specify a specific exception to be caught here, you will enter here
     *
     * @param: request: request
     * @param: exception: error
     * @return: com.mds.portable.common.message.Message
     * @auther: Sopp
     * @date: 2019/4/19 10:04
     */
    @ExceptionHandler(value = Exception.class)
    public Message error(HttpServletRequest request, Exception exception) {
        log.info("request url：" + request.getRequestURI());
        log.error(exception.getMessage());
        exception.printStackTrace();
        return Message.error(exception.getMessage());
    }

    /**
     * Functional description: The method of intercepting http requests.
     *
     * @param request
     * @return com.mds.portable.common.message.Message
     * @throws
     * @auther Sopp
     * @date: 2019/4/25 9:42
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Message httpRequestMethodException(HttpServletRequest request) {
        log.error("The current request method is incorrect and cannot be used " + request.getMethod() + "method");
        return Message.methodError();
    }
}
