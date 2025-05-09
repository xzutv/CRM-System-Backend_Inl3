package se.yrgo.advice;

import org.aspectj.lang.*;

public class PerformanceTimingAdvice {

    public Object performTimingMeasurment(ProceedingJoinPoint method) throws Throwable {

        long startTime = System.nanoTime();
        try {
            Object value = method.proceed();
            return value;
        } finally {

            long endTime = System.nanoTime();
            long timeTaken = endTime - startTime;

            System.out.println("The method " + method.getSignature().getName() + " took " + timeTaken / 1000000);
        }
    }
}