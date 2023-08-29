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
package com.hyts.assemble.resource.drs.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @project-name:lhy-report
 * @package-name:com.lhy.report.resource.lock
 * @author:LiBo/Alex
 * @create-date:2022-05-16 22:50
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
public class LocalDRSPoolResourceLocker implements DRSPoolResourceLocker<Object>{



    Lock lock = new ReentrantLock();

    /**
     * 加锁
     * @param key
     * @return
     */
    @Override
    public boolean lock(Object key) {
        return lock.tryLock();
    }

    /**
     * 加锁
     * @param key
     * @param timeout
     * @param timeUnit
     * @return
     */
    @Override
    public boolean lock(Object key, long timeout, TimeUnit timeUnit) {
        try {
            return lock.tryLock(timeout,timeUnit);
        } catch (InterruptedException e) {
            log.error("lock is abort!",e);
            return false;
        }
    }

    /**
     * 解锁
     * @param key
     * @return
     */
    @Override
    public boolean unLock(Object key) {
        try {
            lock.unlock();
            return Boolean.TRUE;
        }catch (Exception e){
            log.error("unlock is failure!",e);
            return Boolean.FALSE;
        }
    }

    /**
     * 解锁
     * @param key
     * @param timeout
     * @param timeUnit
     * @return
     */
    @Override
    public boolean unLock(Object key, long timeout, TimeUnit timeUnit) {
        try {
            lock.unlock();
            return Boolean.TRUE;
        }catch (Exception e){
            log.error("unlock is failure!",e);
            return Boolean.FALSE;
        }
    }
}
