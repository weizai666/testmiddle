/**
 * Copyright [2019] [LiBo/Alex of copyright liboware@gmail.com ]
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
package com.hyts.assemble.redisdelayer.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @project-name:wiz-shrding-framework
 * @package-name:com.wiz.sharding.framework.boot.starter.redisson.delayed.listener
 * @author:LiBo/Alex
 * @create-date:2021-08-13 9:54
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */


@RequiredArgsConstructor
@Slf4j
public final class ExecutableExceptionHandler implements Thread.UncaughtExceptionHandler {


    final List<DelayedExceptionHandler> exceptionHandlers;

    /**
     * 操作机制控制异常处理
     * @param t
     * @param e
     */
    @Override
    public void uncaughtException(Thread t, Throwable e){
        try {
            if(CollectionUtils.isNotEmpty(exceptionHandlers)) {
                exceptionHandlers.stream().forEach(param -> {
                    param.catchException(e, t);
                });
            }
        } catch (Exception e1) {
            log.error("Failed to execute exception capture mechanism!",e);
        }
    }

}
