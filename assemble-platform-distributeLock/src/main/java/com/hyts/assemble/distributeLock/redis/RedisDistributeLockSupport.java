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
package com.hyts.assemble.distributeLock.redis;

import com.hyts.assemble.distributeLock.common.AbstractDistributeLockSupport;
import com.hyts.assemble.distributeLock.base.DistributeLockParam;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.extense.dlock.redis
 * @author:LiBo/Alex
 * @create-date:2021-12-11 6:19 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @descriptioTn:
 */
@Slf4j
@Component
public class RedisDistributeLockSupport extends AbstractDistributeLockSupport<RLock> {



    @Autowired
    RedissonClient redissonClient;


    /**
     * 非阻塞方式锁
     * @param distributeLockParam
     * @return
     */
    @Override
    public RLock lock(DistributeLockParam distributeLockParam) {
        distributeLockParam = fullDistributeDefaultValue(distributeLockParam);
        String lockKey = buildLockKey(distributeLockParam);
        RLock rLock = null;
        try {
            switch (distributeLockParam.getLockType()) {
                // 可重入锁
                case REENTRANT_LOCK: {
                    rLock = redissonClient.getLock(lockKey);
                    break;
                }
                // 非公平锁
                case FAIR_LOCK: {
                    rLock = redissonClient.getFairLock(lockKey);
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("暂时不支持此种方式的锁!");
                }
            }
            Boolean result = rLock.tryLock(distributeLockParam.getWaitTime(), distributeLockParam.getExpireTime(), distributeLockParam.getTimeUnit());
            return rLock;
        } catch (InterruptedException e) {
            log.error("加锁为阻塞模式下的锁进行失败！", e);
            return rLock;
        }
    }



    @Override
    public void unlock(RLock param, DistributeLockParam distributeLockParam) {
        try {
            param.unlock();
        } catch (Exception e) {
            log.error("解锁为阻塞模式下的锁进行失败！", e);
        }
    }


}
