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
package com.hyts.assemble.eventbus.spring;

import com.hyts.assemble.eventbus.EventListener;
import com.hyts.assemble.eventbus.EventListenerRegistry;
import com.hyts.assemble.eventbus.disruptor.EventModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.extense.event.spring
 * @author:LiBo/Alex
 * @create-date:2021-12-08 10:52 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
//@ConditionalOnExpression("'${event-bus.model}'.equalsIgnoreCase('spring')")
@Component("spring")
public class SpringEventListenerRegistry implements EventListenerRegistry<EventModel> {


    @Autowired
    ApplicationContext applicationContext;



    final List<EventListener> eventListeners;


    public SpringEventListenerRegistry(List<EventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }



    @PostConstruct
    public void init(){
        log.info("开始初始化Spring事件监听器的组件服务");
        initRegistryEventListener(eventListeners);
        log.info("完成初始化Spring事件监听器的组件服务：{}",eventListeners);
    }


    @Override
    public void initRegistryEventListener(List<EventListener> eventConsumerList) {

    }


    @Override
    public void publish(EventModel param) {
        applicationContext.publishEvent(param);
    }
}
