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
package com.hyts.assemble.apigateway.route;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.apigateway.route
 * @author:LiBo/Alex
 * @create-date:2022-06-05 11:39
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@Component
public class NacosDynamicRouteService implements ApplicationEventPublisherAware {


    @Value("${spring.application.name:api-gateway}")
    private String dataId;


    @Value("${spring.cloud.nacos.config.group:DEFAULT_GROUP}")
    private String group;


    @Value("${spring.cloud.nacos.config.file-extension:properties}")
    private String extenseName;


    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;


    public static final long DEFAULT_TIMEOUT = 30000;

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;


    private static final List<String> ROUTE_LIST = Lists.newArrayList();

    /**
     * 注入事件多播器
     * @param applicationEventPublisher
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 发布事件进行更新配置
     */
    private void publish() {
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this.routeDefinitionWriter));
    }

    /**
     * 增加网关启动时，更新一次配置
     */
    @PostConstruct
    public void init() {
        log.info("gateway route init...");

        try{
            ConfigService configService = NacosFactory.createConfigService(serverAddr);

            if(configService == null){
                log.warn("initConfigService fail");
                return;
            }
            String dataIdStr = dataId+"."+extenseName;
            log.info("load the nacos config is {},{}",dataIdStr,group);
            String configInfo = configService.getConfig(dataIdStr, group, DEFAULT_TIMEOUT);
            log.info("获取网关当前配置:\r\n{}",configInfo);
            List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
            for(RouteDefinition definition : definitionList){
                log.info("update route : {}",definition.toString());
                addRoute(definition);
            }
        } catch (Exception e) {
            log.error("初始化网关路由时发生错误",e);
        }
    }

    /**
     * 动态刷新路由控制
     */
    @PostConstruct
    public void dynamicRouteByNacosListener() {
        try {
            ConfigService configService = NacosFactory.createConfigService(serverAddr);
            String dataIdStr = dataId+"."+extenseName;
            log.info("load the nacos config is {},{}",dataIdStr,group);
            configService.getConfig(dataIdStr, group, 5000);
            configService.addListener(dataIdStr, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    // 手动新配置以后，先清除原来的配置
                    clearRoute();
                    try {

                        List<RouteDefinition> gatewayRouteDefinitions = JSONObject.parseArray(configInfo, RouteDefinition.class);
                        for (RouteDefinition routeDefinition : gatewayRouteDefinitions) {
                            addRoute(routeDefinition);
                        }
                        publish();
                    } catch (Exception e) {
                        log.error("refresh gateway route is failure!",e);
                    }
                }
                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            log.error("execute the refresh the route condition datas",e);
        }
    }

    /**
     * 清理路由草自作
     */
    private void clearRoute() {
        for(String id : ROUTE_LIST) {
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
        }
        ROUTE_LIST.clear();
    }

    /**
     * 添加路由操作
     * @param definition
     */
    private void addRoute(RouteDefinition definition) {
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            ROUTE_LIST.add(definition.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
