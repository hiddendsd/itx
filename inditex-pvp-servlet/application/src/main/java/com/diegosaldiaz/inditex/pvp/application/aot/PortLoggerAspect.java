package com.diegosaldiaz.inditex.pvp.application.aot;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect to log the traffic through the ports.
 * Any port call is logged as well as its return.
 */
@Component
@Aspect
@Slf4j
public class PortLoggerAspect {

  /**
   * Pointcut that matches all implementations of the interfaces defined in port.inbound package.
   */
  @Pointcut("within(com.diegosaldiaz.inditex.pvp.application.port.*.*+)")
  public void spiPortImplementationsPointcut() {
    // Method is empty as this is just a Pointcut, the implementations are in the advices.
  }

  /**
   * Time measurement and in-out info logging around any ports.spi implementation .
   */
  @Around("spiPortImplementationsPointcut()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    Instant start = Instant.now();
    debugIfPossible("PORT TRACER - Enter: {}.{}() with argument[s] = {}",
        joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(),
        Arrays.toString(joinPoint.getArgs()));
    try {
      Object result = joinPoint.proceed();
      long timeElapsed = Duration.between(start, Instant.now()).toMillis();
      debugIfPossible("PORT TRACER - Exit in {} ms: {}.{}() with result = {}",
          timeElapsed,
          joinPoint.getSignature().getDeclaringTypeName(),
          joinPoint.getSignature().getName(),
          result);
      return result;
    } catch (IllegalArgumentException e) {
      log.warn("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
          joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
      throw e;
    }
  }

  private void debugIfPossible(String message, Object... params) {
    if (log.isDebugEnabled()) {
      log.debug(message, params);
    }
  }
}
