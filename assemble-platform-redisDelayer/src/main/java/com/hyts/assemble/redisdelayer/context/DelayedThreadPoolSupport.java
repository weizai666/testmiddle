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
package com.hyts.assemble.redisdelayer.context;

import lombok.Getter;

import java.util.concurrent.Executor;

/**
 * @project-name:wiz-shrding-framework
 * @package-name:com.wiz.sharding.framework.boot.starter.redisson.delayed.context
 * @author:LiBo/Alex
 * @create-date:2021-08-11 16:34
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class DelayedThreadPoolSupport {

    /**
     * 任务执行线程机制
     */
    @Getter
    private static Executor taskExecuteThread;

    /**
     * 任务轮询线程机制
     */
    @Getter
    private static Executor taskRecycleThread;


    /**
     * 操作处理机制
     * @param taskExecuteThread
     * @param taskRecycleThread
     */
    public DelayedThreadPoolSupport(Executor taskExecuteThread, Executor taskRecycleThread) {
        DelayedThreadPoolSupport.taskExecuteThread = taskExecuteThread;
        DelayedThreadPoolSupport.taskRecycleThread = taskRecycleThread;
    }
}
