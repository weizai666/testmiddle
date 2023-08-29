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

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @project-name:wiz-shrding-framework
 * @package-name:com.wiz.sharding.framework.boot.starter.redisson.delayed.context
 * @author:LiBo/Alex
 * @create-date:2021-08-11 16:04
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class DelayedThreadPoolExecutor {


    /**
     * 获取到服务器的cpu内核：逻辑内核核心数
     */
    private static int DEFAULT_THREAD_CORE_BASE_SIZE = Runtime.getRuntime().availableProcessors();

    /**
     * IO密集型机制控制*2
     */
    private static int DEFAULT_THREAD_CORE_SIZE_IO_TYPE = DEFAULT_THREAD_CORE_BASE_SIZE<<1;


    /**
     * 序号分配器
     */
    private static  AtomicInteger atomicInteger = new AtomicInteger();


    /**
     * 初始化参数信息
     * @return
     */
    public static ThreadPoolTaskExecutor initParameter(String threadGroup){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(DEFAULT_THREAD_CORE_SIZE_IO_TYPE);//核心池大小
        executor.setMaxPoolSize(DEFAULT_THREAD_CORE_SIZE_IO_TYPE<<4);//最大线程数 = 核心*核心池大小;
        executor.setQueueCapacity(1000);//队列程度
        executor.setKeepAliveSeconds(30);//线程空闲时间
        executor.setThreadGroupName(threadGroup);
        executor.setThreadFactory(r -> new Thread(r,String.format("%s-%s",threadGroup,atomicInteger.getAndDecrement())));
        executor.setThreadNamePrefix(threadGroup+"-");//线程前缀名称
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());//配置拒绝策略
        return executor;
    }




}
