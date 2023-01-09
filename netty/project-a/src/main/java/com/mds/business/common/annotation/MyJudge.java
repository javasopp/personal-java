package com.mds.business.common.annotation;

import java.lang.annotation.*;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/6/28 17:03
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyJudge {
    String value() default "";
}
