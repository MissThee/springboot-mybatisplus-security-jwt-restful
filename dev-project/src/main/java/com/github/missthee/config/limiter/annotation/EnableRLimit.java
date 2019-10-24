package com.github.missthee.config.limiter.annotation;

import com.github.missthee.config.limiter.impl.enablelimit.EnableRLimitImp;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用@RLimit注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(value = EnableRLimitImp.class)
public @interface EnableRLimit {
}
