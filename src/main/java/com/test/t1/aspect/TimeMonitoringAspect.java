package com.test.t1.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeMonitoringAspect {

    @Pointcut("@annotation(com.test.t1.annotation.TimeMonitoring)")
    public void customTimeMonitoringPointcut() {}

    @Around("customTimeMonitoringPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("Замер метода {} начался", joinPoint.getSignature().getName());
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("Метод {} выбросил исключение на земере времени", joinPoint.getSignature().getName());
            throw throwable;
        }
        long end = System.currentTimeMillis();
        log.info("Замер метода {} закончился", joinPoint.getSignature().getName());
        log.info("Время выполнения метода {}: {} ms", joinPoint.getSignature().getName(), end - start);
        return result;
    }
}
