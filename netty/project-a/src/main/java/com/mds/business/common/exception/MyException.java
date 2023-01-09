package com.mds.business.common.exception;

/**
 * @ClassName: PermissionException
 * @Description: Permission exception
 * @Author: Hower
 * @Date: 2019/6/5 15:43
 **/
public class MyException extends RuntimeException {
    public MyException(String message) {
        super(message);
    }
}