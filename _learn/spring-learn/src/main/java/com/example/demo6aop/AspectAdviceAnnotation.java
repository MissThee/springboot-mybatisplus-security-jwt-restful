package com.example.demo6aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class AspectAdviceAnnotation {
    @Pointcut("execution(* com.example.demo6aop.Teacher.*(..))")
    private void myPointcut1() {
    }

    @Before("myPointcut1()")
    public void beforeAdvice() {
        System.out.println("--beforeAdvice");
    }

    @After("myPointcut1()")
    public void afterAdvice() {
        System.out.println("--afterAdvice");
    }

    @AfterReturning(pointcut = "myPointcut1()", returning = "retVal")
    public void afterReturningAdvice(Object retVal) {
        System.out.println("--afterReturningAdvice:" + retVal.toString());
    }

    @AfterThrowing(pointcut = "myPointcut1()", throwing = "ex")
    public void afterThrowingAdvice(IllegalArgumentException ex) {
        System.out.println("--afterThrowingAdvice: " + ex.toString());
    }

    @Around("myPointcut1()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("--aroundAdvice:before");
        Object returnValue = joinPoint.proceed();
        System.out.println("--aroundAdvice:after");
        return returnValue;
    }
}