/**
 * Copyright [2020] [LiBo/Alex of copyright liboware@gmail.com ]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyts.assemble.circuitbreaker.handle;

import com.hyts.assemble.circuitbreaker.CircuitBreakerMonitor;
import com.hyts.assemble.circuitbreaker.annon.CircuitBreakerControl;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.configure.CircuitBreakerAspect;
import io.github.resilience4j.circuitbreaker.configure.CircuitBreakerConfigurationProperties;
import io.github.resilience4j.circuitbreaker.utils.CircuitBreakerUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.circuitbreaker.handle
 * @author:LiBo/Alex
 * @create-date:2022-07-18 23:10
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Aspect
@Component
@Slf4j
public class CircuitBreakerHandler{


    private final CircuitBreakerConfigurationProperties circuitBreakerProperties;


    private final CircuitBreakerRegistry circuitBreakerRegistry;


    public CircuitBreakerHandler(CircuitBreakerConfigurationProperties backendMonitorPropertiesRegistry,
                                CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerProperties = backendMonitorPropertiesRegistry;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    /**
     * aop切面处理
     * @param circuitBreaker
     */
    @Pointcut(value = "@within(circuitBreaker) || @annotation(circuitBreaker)", argNames = "circuitBreaker")
    public void matchAnnotatedClassOrMethod(CircuitBreakerControl circuitBreaker) {
    }

    /**
     * 环绕处理
     * @param proceedingJoinPoint
     * @param backendMonitored
     * @return
     * @throws Throwable
     */
    @Around(value = "matchAnnotatedClassOrMethod(backendMonitored)",
            argNames = "proceedingJoinPoint, backendMonitored")
    public Object circuitBreakerAroundAdvice(ProceedingJoinPoint proceedingJoinPoint, CircuitBreakerControl backendMonitored) throws Throwable {
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        String methodName = method.getDeclaringClass().getName() + "#" + method.getName();
        if (backendMonitored == null) {
            backendMonitored = getBackendMonitoredAnnotation(proceedingJoinPoint);
        }
        String backend = backendMonitored.resourceName();
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = getOrCreateCircuitBreaker(methodName, backend);
        return handleJoinPoint(proceedingJoinPoint, circuitBreaker, methodName);
    }

    /**
     * 获取以及创建对应的操作
     * @param methodName
     * @param backend
     * @return
     */
    private io.github.resilience4j.circuitbreaker.CircuitBreaker getOrCreateCircuitBreaker(String methodName, String backend) {
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(backend,
                () -> circuitBreakerProperties.createCircuitBreakerConfig(backend));
        CircuitBreakerMonitor.addCircuitBreakerListener(circuitBreaker);
        if (log.isInfoEnabled()) {
            log.info("Created or retrieved circuit breaker '{}' with failure rate '{}' and wait interval'{}' for method: '{}'",
                    backend, circuitBreaker.getCircuitBreakerConfig().getFailureRateThreshold(),
                    circuitBreaker.getCircuitBreakerConfig().getWaitDurationInOpenState(), methodName);
        }
        return circuitBreaker;
    }

    /**
     * 获取后端方法的监控注解
     * @param proceedingJoinPoint
     * @return
     */
    private CircuitBreakerControl getBackendMonitoredAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
        if (log.isInfoEnabled()) {
            log.info("circuitBreaker parameter is null");
        }
        CircuitBreakerControl circuitBreaker = null;
        Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
        if (targetClass.isAnnotationPresent(CircuitBreakerControl.class)) {
            circuitBreaker = targetClass.getAnnotation(CircuitBreakerControl.class);
            if (circuitBreaker == null) {
                if (log.isInfoEnabled()) {
                    log.info("TargetClass has no annotation 'CircuitBreaker'");
                }
                circuitBreaker = targetClass.getDeclaredAnnotation(CircuitBreakerControl.class);
                if (circuitBreaker == null) {
                    if (log.isInfoEnabled()) {
                        log.info("TargetClass has no declared annotation 'CircuitBreaker'");
                    }
                }
            }
        }
        return circuitBreaker;
    }


    private Object handleJoinPoint(ProceedingJoinPoint proceedingJoinPoint,
                                   io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker, String methodName) throws Throwable {
        CircuitBreakerUtils.isCallPermitted(circuitBreaker);
        long start = System.nanoTime();
        try {
            Object returnValue = proceedingJoinPoint.proceed();

            long durationInNanos = System.nanoTime() - start;
            circuitBreaker.onSuccess(durationInNanos);
            return returnValue;
        } catch (Throwable throwable) {
            long durationInNanos = System.nanoTime() - start;
            circuitBreaker.onError(durationInNanos, throwable);
            if (log.isDebugEnabled()) {
                log.debug("Invocation of method '" + methodName + "' failed!", throwable);
            }
            throw throwable;
        }
    }



}
