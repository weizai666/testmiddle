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
package com.hyts.assemble.dubbo3.comp.multi;

import com.google.common.collect.Lists;
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
public class MultiProviderBoostrap {


    private static String zookeeperHost = System
            .getProperty("zookeeper.address", "127.0.0.1");

    private static String zookeeperPort = System.getProperty("zookeeper.port",
            "2181");

    private static String zookeeperHost2 = System
            .getProperty("zookeeper.address", "127.0.0.1");

    private static String zookeeperPort2 = System.getProperty("zookeeper.port",
            "2183");

    public static <T> void export(Class<T> clazz,Class clazzImpl){
        try {
            ServiceConfig<T> service = new ServiceConfig<>();
            service.setApplication(new ApplicationConfig("ProviderBoostrap"));
            service.setRegistries(Lists.newArrayList(new RegistryConfig(
                    "zookeeper://" + zookeeperHost + ":" + zookeeperPort),new RegistryConfig(
                    "zookeeper://" + zookeeperHost2 + ":" + zookeeperPort2)));
            service.setInterface(clazz);
            service.setRef((T)clazzImpl.newInstance());
//            service.setCache("redis");
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
        MultiProviderBoostrap.export(BaseProcessService.class, DefaultBaseProcessService.class);
        MultiProviderBoostrap.await();
    }

}
