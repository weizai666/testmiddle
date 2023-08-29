package com.hyts.assemble.distributeLock.redis;

import java.lang.annotation.*;

/**
 * describe:
 *
 * @author hxy
 * @date 2020-10-26
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisDistributedLockParam {
    String name() default "";
}
