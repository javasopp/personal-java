package com.mds.business.common.annotation;

import java.lang.annotation.*;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/7/15 10:19
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PackageJudge {
    String value() default "";
}
