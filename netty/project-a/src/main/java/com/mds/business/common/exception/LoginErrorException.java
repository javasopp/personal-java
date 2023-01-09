package com.mds.business.common.exception;

/**
 * 处理登录相关的跳转-前端要求
 * @author sopp
 * @version 1.0
 * @date 2021/9/16 10:14
 */
public class LoginErrorException extends Exception {

    public LoginErrorException(String message) {
        super(message);
    }

}
