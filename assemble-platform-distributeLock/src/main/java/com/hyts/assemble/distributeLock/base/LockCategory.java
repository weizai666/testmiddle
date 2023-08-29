package com.hyts.assemble.distributeLock.base;

/**
 * @author ： jyy
 * @dateTime ：2021/12/1 3:09 下午
 * <h2>redis 锁类型</h2>
 */
public enum LockCategory {
    COMMON,
    SPIN,
    FAIR;
}
