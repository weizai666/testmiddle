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
package com.hyts.assemble.redisdelayer.redis;

import com.hyts.assemble.redisdelayer.model.ExecuteInvokerEvent;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;

import java.util.Objects;

/**
 * @project-name:wiz-shrding-framework
 * @package-name:com.wiz.sharding.framework.boot.starter.redisson.delayed
 * @author:LiBo/Alex
 * @create-date:2021-08-11 15:34
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: RedissionClientTool工具类实现的具体是实现，覆盖一些需要定制化的阻塞队列方法
 */

@AutoConfigureAfter(value = RedissonClientTool.class)
@Slf4j
public class DelayedRedissionClientTool  {


    /**
     * redissionCLientTool工具机制
     */
    @Autowired
    RedissonClientTool redissonClientTool;


    /**
     * 自动注册
     */
    public DelayedRedissionClientTool() {
    }

    /**
     * 手动注册
     * @param redissonClientTool
     */
    public DelayedRedissionClientTool(RedissonClientTool redissonClientTool) {
        this.redissonClientTool = redissonClientTool;
    }

    /**
     * 添加阻塞队列-元素
     * @param <T>
     */
    public <T> void offer(ExecuteInvokerEvent<T> executeInvokerEvent) {
        //预先进行构建初始化参数条件机制
        executeInvokerEvent.preCondition(executeInvokerEvent);
        redissonClientTool.addDelayQueueElement(Objects.requireNonNull(executeInvokerEvent).getBizGroup(),
                executeInvokerEvent,executeInvokerEvent.getDelayedTime(),executeInvokerEvent.getTimeUnit());
    }


    /**
     * 获取相关的
     * @param executeInvokerEvent
     * @param <T>
     * @return
     */
    public <T> RBlockingQueue<T> takeBlockingQueue(ExecuteInvokerEvent<T> executeInvokerEvent) {
        return redissonClientTool.getRedissonClient().getBlockingQueue(executeInvokerEvent.getBizGroup());

    }

    /**
     * 操作梳理
     * @param trBlockingQueue
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public <T> ExecuteInvokerEvent<T> poll(RBlockingQueue<T> trBlockingQueue) throws InterruptedException {
        return (ExecuteInvokerEvent<T>) trBlockingQueue.take();
    }

}
