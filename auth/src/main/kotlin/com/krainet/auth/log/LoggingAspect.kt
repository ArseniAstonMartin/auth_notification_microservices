package com.krainet.auth.log

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {

    private val log = LoggerFactory.getLogger(javaClass)

    @Before("execution(* com.krainet.auth.controller..*(..))")
    fun logBeforeControllerMethod(joinPoint: JoinPoint) {
        log.info("Controller call: {}", joinPoint.signature.toShortString())
    }

    @AfterReturning(pointcut = "execution(* com.krainet.auth.controller..*(..))", returning = "result")
    fun logAfterControllerMethod(joinPoint: JoinPoint, result: Any?) {
        log.info("Controller success: {}", joinPoint.signature.toShortString())
    }

    @AfterThrowing(
        pointcut = "execution(* com.krainet.auth.controller..*(..)) || execution(* com.krainet.auth.service..*(..))",
        throwing = "ex",
    )
    fun logAfterThrowing(joinPoint: JoinPoint, ex: Throwable) {
        log.error(
            "Exception in {}: {} - {}",
            joinPoint.signature.toShortString(),
            ex.javaClass.simpleName,
            ex.message,
        )
    }
}
