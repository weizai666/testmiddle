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

import com.hyts.assemble.redisdelayer.listener.EventExecutableInvokerListener;
import com.hyts.assemble.redisdelayer.listener.ExecutableExceptionHandler;
import com.hyts.assemble.redisdelayer.model.ExecuteInvokerEvent;
import com.hyts.assemble.redisdelayer.redis.DelayedRedissionClientTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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

@RequiredArgsConstructor
@Slf4j
public  class DelayedBootstrapRunnable implements Runnable{



    /**
     * 直接传递相关的执行客户端访问器
     */
    public DelayedRedissionClientTool delayedRedissionClientTool = DelayedRedisClientSupport.getDelayedRedissionClientTool();


    /**
     * 绑定的线程组，只会执行相关的线程组之间的关系机制
     */
    public final String bizGroup;

    /**
     * 注入参数进入
     */
    public final List<EventExecutableInvokerListener> eventExecutableInvokerListeners;

    /**
     * 执行线程池
     */
    public final Executor executorThreadPool;


    /**
     * 异常信息控制
     */
    public final ExecutableExceptionHandler exceptionHandlers;


    /**
     * 启动服务处理机制
     */
    @Override
    public void run() {
        try {
            RBlockingQueue<ExecuteInvokerEvent> blockingQueue = delayedRedissionClientTool.takeBlockingQueue(new ExecuteInvokerEvent(bizGroup));
            Executor executor = Objects.isNull(executorThreadPool) ? DelayedThreadPoolSupport.getTaskExecuteThread() : executorThreadPool;
            Thread.currentThread().setUncaughtExceptionHandler(exceptionHandlers);
            for(;;) {
               try{
                   ExecuteInvokerEvent data =  delayedRedissionClientTool.poll(blockingQueue);
                   log.info("侦听队列任务组：{},获得值:{}", bizGroup, data);
                   log.info(MessageFormat.format("【1】Execute parse complete call: the execution time should be：{0,date,yyyy-MM-dd HH:mm:ss}，" +
                                   "Actual execution time：{1,date,yyyy-MM-dd HH:mm:ss},createTime：{2,date,yyyy-MM-dd HH:mm:ss}",
                           data.getFiredTime(), new Date(),new Date(data.getCreateTime())));
                   executor.execute(() -> {
                       for(EventExecutableInvokerListener eventExecutableInvokerListener : eventExecutableInvokerListeners){
                           eventExecutableInvokerListener.handle(data);
                       }
                   });
               }catch (Exception e){
                   log.error("无法执行处理",e);
               }
            }
        } catch (Exception e) {
            log.error("无法执行处理",e);
//            throw new RuntimeException(e);
        }
    }



}
