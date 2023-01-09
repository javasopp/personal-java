package com.sopp.all.common.annotation;

import java.lang.annotation.*;

/**
 * configuration log info
 * @author Sopp
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyLog {
    String value() default "";
}
