package com.example.demo6aop;

import org.aspectj.lang.ProceedingJoinPoint;

public class AspectAdvice {

    public void beforeAdvice() {
        System.out.println("--beforeAdvice");
    }

    public void afterAdvice() {
        System.out.println("--afterAdvice");
    }

    public void afterReturningAdvice(Object returnValue) {
        System.out.println("--afterReturningAdvice: " + returnValue);
    }

    public void afterThrowingAdvice(Throwable e) {
        System.out.println("--afterThrowingAdvice: " + e);
    }

    public Object aroundAdvice(ProceedingJoinPoint point) throws Throwable {
        System.out.println("--aroundAdvice:before");
        Object returnValue = point.proceed();
        System.out.println("--aroundAdvice:after");
        return returnValue;
    }
}