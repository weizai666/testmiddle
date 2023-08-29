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
package com.hyts.assemble.dubbo3.comp.api;

import com.hyts.assemble.dubbo3.comp.service.BaseProcessService;
import com.hyts.assemble.dubbo3.comp.service.impl.DefaultBaseProcessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 * @project-name:assemble
 * @package-name:com.hyts.assemble.dubbo3.comp.api
 * @author:LiBo/Alex
 * @create-date:2022-06-06 13:41
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 服务提供者配置信息
 */
@Slf4j
public class APIProviderBoostrap {


    private static String zookeeperHost = System
            .getProperty("zookeeper.address", "127.0.0.1");


    private static String zookeeperPort = System.getProperty("zookeeper.port",
            "2181");


    public static <T> void export(Class<T> clazz,Class clazzImpl){
        try {
            ServiceConfig<T> service = new ServiceConfig<>();
            // 设置对应的Application应用配置
            service.setApplication(new ApplicationConfig("ProviderBoostrap"));
            // 设置注册中心
            service.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":" + zookeeperPort));
            service.setInterface(clazz);
            service.setRef((T)clazzImpl.newInstance());
            service.export();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("export the service provider is failure!",e);
        }
    }


    public static void await(){
        try {
            log.info("dubbo service started");
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            log.error("dubbo service started is failure!",e);
        }
    }


    public static void main(String[] args){
        APIProviderBoostrap.export(BaseProcessService.class, DefaultBaseProcessService.class);
        APIProviderBoostrap.await();
    }

}
