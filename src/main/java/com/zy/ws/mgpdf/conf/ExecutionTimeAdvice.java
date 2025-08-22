package com.zy.ws.mgpdf.conf;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAdvice {

    @Around("@annotation(com.zy.ws.mgpdf.conf.TrackExecutionTime)")
    public Object executionTime(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();

        // Method Information
        MethodSignature signature = (MethodSignature) point.getSignature();
        // Method annotation
        Method method = signature.getMethod();
        TrackExecutionTime trackExecutionTime = method.getAnnotation(TrackExecutionTime.class);
        String operation = trackExecutionTime.operation();

        Object object = point.proceed();
        long endtime = System.currentTimeMillis();

        log.info("{operation:{},duration:{}}",operation,(endtime-startTime)/1000f);
        return object;
    }
}