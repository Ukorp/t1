package com.test.t1.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.test.t1.annotation.CustomLog)")
    public void customLogPointcut() {}

    @Before("customLogPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Метод {} вызван. Аргументы: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "customLogPointcut()",
            returning = "result"
    )
    public void logAfterReturn(JoinPoint joinPoint, Object result) {
        log.info("Метод {} вызван вернул объект класса: {}", joinPoint.getSignature().getName(),
                result != null ?
                result.getClass() :
                null);
    }

    @AfterThrowing(pointcut = "customLogPointcut()",
            throwing = "exception"
    )
    public void logAfterThrow(JoinPoint joinPoint, Exception exception) {
        log.warn("Метод {} выбросил исключение: {}", joinPoint.getSignature().getName(), exception.getMessage());
    }
}
