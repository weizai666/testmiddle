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
package com.hyts.assemble.redisdelayer.sample;

import com.alibaba.fastjson.JSONObject;
import com.hyts.assemble.redisdelayer.anno.DelayedQueueListener;
import com.hyts.assemble.redisdelayer.listener.EventExecutableInvokerListener;
import com.hyts.assemble.redisdelayer.listener.ExecutableInvokerListener;
import com.hyts.assemble.redisdelayer.model.ExecuteInvokerEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;

/**
 * @project-name:assemble
 * @package-name:com.hyts.assemble.minio.delayed
 * @author:LiBo/Alex
 * @create-date:2022-05-24 20:06
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@DelayedQueueListener(value="delayedQueueListener",group = "test_delayed_queue")
public class DefaultDelayedQueueListener implements EventExecutableInvokerListener<Object,Object> {

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public Object handle(ExecuteInvokerEvent<Object> param) {
        log.info("input the parameter:{}",param);
        return param;
    }
}
