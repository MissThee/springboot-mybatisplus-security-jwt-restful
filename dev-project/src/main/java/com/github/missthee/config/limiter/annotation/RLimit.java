package com.github.missthee.config.limiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 默认以 [用户token][用户ip][请求的uri][请求类型]为识别，限制controller请求的间隔
 * 仅可用在方法名称以Controller结尾方法上生效
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RLimit {

    int minTime() default 100;//默认访问最小间隔时间。毫秒

    String msg() default "";//访问过于频繁时，回传的msg。
}
