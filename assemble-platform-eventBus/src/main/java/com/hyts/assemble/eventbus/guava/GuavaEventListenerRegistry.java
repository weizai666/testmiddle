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
package com.hyts.assemble.eventbus.guava;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.hyts.assemble.eventbus.EventListener;
import com.hyts.assemble.eventbus.EventListenerRegistry;
import com.hyts.assemble.eventbus.disruptor.EventModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.objenesis.instantiator.util.ClassUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.extense.event.guava
 * @author:LiBo/Alex
 * @create-date:2021-12-04 9:39 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
//@ConditionalOnExpression("'${event-bus.model}'.equalsIgnoreCase('guava')")
@Component("guava")
public class GuavaEventListenerRegistry implements EventListenerRegistry<EventModel> {


    EventBus eventBus;


    final List<EventListener> eventListeners;

    /**
     * 构造器方式的注入方式
     * @param eventListeners
     */
    public GuavaEventListenerRegistry(List<EventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }


    @PostConstruct
    public void init(){
        log.info("开始初始化Guava事件监听器的组件服务");
        initRegistryEventListener(eventListeners);
        log.info("完成初始化Guava事件监听器的组件服务");
    }


    /**
     * 注册监听器操作
     * @param eventConsumerList
     */
    @Override
    public void initRegistryEventListener(List<EventListener> eventConsumerList) {
        Executor executor = ThreadUtil.newExecutor(10,20,300);
        eventBus = new AsyncEventBus(GuavaEventListenerRegistry.class.getName(),executor);
        eventConsumerList.stream().forEach(param->{
            log.info("注册服务监听器:{}",param.getClass());
            eventBus.register(ClassUtils.newInstance(param.getClass()));
        });
    }


    /**
     * 发布事件操作
     * @param param
     */
    @Override
    public void publish(EventModel param) {
        eventBus.post(param);
    }

    
}
