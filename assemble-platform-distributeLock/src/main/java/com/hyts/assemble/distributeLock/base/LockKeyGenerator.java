package com.hyts.assemble.distributeLock.base;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * describe:
 *
 * @author hxy
 * @date 2020-10-26
 */
public interface LockKeyGenerator {
    String getLockKey(ProceedingJoinPoint pjp);
}
