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
package com.hyts.assemble.eventbus;

import com.google.common.eventbus.Subscribe;
import com.hyts.assemble.eventbus.EventListener;
import com.hyts.assemble.eventbus.disruptor.EventModel;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.engine.listener
 * @author:LiBo/Alex
 * @create-date:2021-12-08 11:30 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */

@Slf4j
@Component
public abstract class ExecutableEventListener implements EventListener<EventModel<?>>, EventHandler<EventModel<?>> {



    @org.springframework.context.event.EventListener
    @Subscribe
    @Override
    public void onMessage(EventModel<?> message) {
        log.info("系统监听明细执行启动服务监听器:{}",message);
        if(topic().equals(message.getTopic())) {
            handle(message);
        }
    }



    @Override
    public void onEvent(EventModel<?> objectEventModel, long l, boolean b) throws Exception {
//        log.info("接收到执行监听器驱动为:disruptor");
        onMessage(objectEventModel);
    }

    /**
     * 操作处理机制控制！
     * @param message
     */
    public abstract void handle(EventModel message);

}
