package com.wxy.demo;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    double persecoud() default 10;
    long timeout() default 500;
}
