package com.test.ecommerce.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("execution(* com.test.ecommerce.domain.*.controller.*Controller.*(..))")
    public void controllerMethods() {}

    @Pointcut("execution(* com.test.ecommerce.domain.*.service.*Service.*(..))")
    public void serviceMethods() {}

    @Around("controllerMethods() || serviceMethods()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String executionPoint = className.contains("Controller") ? "Controller" : "Service";

        // 1. 메서드 진입 로그 (BEFORE)
        log.info(">>>> [{}] {}.{}() 호출 시작. Args: {}",
                executionPoint, className, methodName, joinPoint.getArgs());

        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            log.error("!!!! [{}] {}.{}() 예외 발생. Error: {}",
                    executionPoint, className, methodName, t.getMessage(), t);
            throw t;
        }

        long end = System.currentTimeMillis();

        // 2. 메서드 종료 로그 (AFTER)
        log.info("<<<< [{}] {}.{}() 호출 종료 ({}ms). Return: {}",
                executionPoint, className, methodName, (end - start), result);

        return result;
    }
}
