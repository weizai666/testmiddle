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
package com.hyts.assemble.distributeLock.common;

import cn.hutool.core.lang.UUID;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.hyts.assemble.distributeLock.base.DistributeLockParam;
import com.hyts.assemble.distributeLock.base.DistributeLockSupport;
import com.hyts.assemble.distributeLock.base.DistributeLockType;
import com.hyts.assemble.distributeLock.redis.RedisDistributedLockParam;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.extense.dlock
 * @author:LiBo/Alex
 * @create-date:2021-12-11 6:36 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public abstract class AbstractDistributeLockSupport<T> implements DistributeLockSupport<T> {



    /**
     * 检验参数
     * @param distributeLockParam
     * @return
     */
    protected DistributeLockParam fullDistributeDefaultValue(DistributeLockParam distributeLockParam){
        Preconditions.checkNotNull(distributeLockParam,"检测到了参数不允许为空！");
        DistributeLockType distributeLockType = distributeLockParam.getLockType();
        distributeLockParam.setLockType(Optional.ofNullable(distributeLockType).orElse(DistributeLockType.FAIR_LOCK));
        distributeLockParam.setExpireTime(Optional.ofNullable(distributeLockParam.getExpireTime()).orElse(DEFAULT_EXPIRE_TIME));
        distributeLockParam.setWaitTime(Optional.ofNullable(distributeLockParam.getExpireTime()).orElse(DEFAULT_WAIT_TIME));
        distributeLockParam.setTimeUnit(Optional.ofNullable(distributeLockParam.getTimeUnit()).orElse(TimeUnit.SECONDS));
        return distributeLockParam;
    }


    /**
     * 构建相关的锁key值
     * @param distributeLockParam
     * @return
     */
    protected String buildLockKey(DistributeLockParam distributeLockParam){
        String lockId = StringUtils.defaultIfEmpty(distributeLockParam.getLockUUid(),
                        UUID.fastUUID().toString());
        distributeLockParam.setLockUUid(lockId);
        String delmiter = StringUtils.defaultIfEmpty(distributeLockParam.getDelimiter(),
                            DEFAULT_DELIMTER);
        distributeLockParam.setDelimiter(delmiter);
        String prefix = StringUtils.defaultIfEmpty(distributeLockParam
                .getLockNamePrefix(),DEFAULT_KEY_PREFIX);
        distributeLockParam.setLockNamePrefix(prefix);
        String lockFullName = "";
        if(!delmiter.equals(DEFAULT_DELIMTER)){
            //todo 待优化
            Joiner joiner = Joiner.on(delmiter).skipNulls();
            lockFullName = joiner.join(prefix,lockId);
        }else{
            lockFullName = DEFAULT_JOINER.join(prefix,lockId);
        }
        return lockFullName;
    }

}
