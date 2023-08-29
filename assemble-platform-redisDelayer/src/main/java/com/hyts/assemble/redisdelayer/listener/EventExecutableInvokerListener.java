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


import com.hyts.assemble.redisdelayer.model.ExecuteInvokerEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @project-name:wiz-shrding-framework
 * @package-name:com.wiz.sharding.framework.boot.starter.redisson.delayed.impl
 * @author:LiBo/Alex
 * @create-date:2021-08-11 15:32
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 业务事件执行器操作的监听接口服务执行
 */

public interface EventExecutableInvokerListener<P,R> extends ExecutableInvokerListener<ExecuteInvokerEvent <P>,R> {

    /**
     * 延时偏移量
     */
    long DEFAULT_DELAYED_OSFFET = 10;

    /**
     * 延时超时时间时间戳
     */
    TimeUnit DEFAULT_DELAYED_TIMEUNIT = TimeUnit.SECONDS;

    /**
     * 是否可以执行异步操作（暂不支持）
     */
    boolean DEFAULT_IS_ASYNC_FLAG = Boolean.TRUE;

    /**
     * 暂时不支持重试机制，会造成数据重复执行机制,主要面向与执行失败后的重试机制（暂不支持）
     */
    int DEFAULT_RETRY_NUM = 0;

    /**
     * 存放在同一个线程执行
     */
    String DEFAULT_BIZ_GROUP = "DEFAULT_GROUP";

    /**
     * 如果没有定义直接采用默认线程池进行执行
     */
     Executor getExecutor();


}
