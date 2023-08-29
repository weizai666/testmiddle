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
package com.hyts.assemble.eventbus.web;

import cn.hutool.extra.spring.SpringUtil;
import com.hyts.assemble.common.model.http.ResultResponse;
import com.hyts.assemble.eventbus.EventListenerRegistry;
import com.hyts.assemble.eventbus.disruptor.EventModel;
import com.hyts.assemble.eventbus.spring.SpringEventListenerRegistry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @project-name:assemble
 * @package-name:com.hyts.assemble.eventbus.web
 * @author:LiBo/Alex
 * @create-date:2022-05-24 21:27
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/api/eventbus")
@Api(value="事件驱动服务",tags = {"事件驱动服务组件"},description = "包含Disruptor、Spring 、Guava三种eventBus")
public class EventBusController {


    @Autowired
    Map<String,EventListenerRegistry> stringEventListenerRegistryMap;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * 创建对应的延时队列对象服务
     * @param event
     * @return
     */
    @PostMapping("/publish")
    @ApiOperation(value="发布事件",notes = "包含Disruptor、Spring 、Guava三种eventBus")
    public ResultResponse create(@RequestParam("driverType") String driverType, @RequestBody EventModel event){
        try {
            EventListenerRegistry eventListenerRegistry =
                    stringEventListenerRegistryMap.getOrDefault(driverType,
                            SpringUtil.getBean(SpringEventListenerRegistry.class));
            log.info("execute the driver class:{}",eventListenerRegistry.getClass().getName());
            eventListenerRegistry.publish(event);
            return ResultResponse.success(event);
        }catch (Exception e){
            log.error("publish event element is failure!",e);
            return ResultResponse.failure("publish event element is failure!");
        }
    }

    @PostMapping("/test")
    @ApiOperation(value="发布事件",notes = "测试事件发布！")
    public ResultResponse test(){
        try {
            applicationContext.publishEvent(new com.hyts.assemble.eventbus.model.EventModel("test"));
            return ResultResponse.success("event");
        }catch (Exception e){
            log.error("publish event element is failure!",e);
            return ResultResponse.failure("publish event element is failure!");
        }
    }






}
