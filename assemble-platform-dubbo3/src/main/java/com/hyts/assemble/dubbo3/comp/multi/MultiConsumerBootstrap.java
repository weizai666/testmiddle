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

import com.hyts.assemble.dubbo3.comp.service.BaseProcessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CountDownLatch;

/**
 * @project-name:assemble
 * @package-name:com.hyts.assemble.dubbo3.comp.api
 * @author:LiBo/Alex
 * @create-date:2022-06-06 13:41
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
public class MultiConsumerBootstrap {


    private static String zookeeperHost = System
            .getProperty("zookeeper.address", "127.0.0.1");

    private static String zookeeperPort = System.getProperty("zookeeper.port",
            "2181");

    /**
     * 引用功能机制
     * @param <T>
     * @param clazz
     */
    public static <T> T reference(Class<T> clazz){
        try {
            ReferenceConfig<T> reference = new ReferenceConfig<>();
            reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
            reference.setRegistry(new RegistryConfig(
                    "zookeeper://" + zookeeperHost + ":" + zookeeperPort));
            reference.setInterface(clazz);
            // cache: threadlocal\jcache(不推荐)\lru - concurrentHashMap
//            reference.setCache("jcache");
            return reference.get();
        } catch (Exception e) {
            log.error("reference the service provider is failure!",e);
            return null;
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
        //DubboTest
        BaseProcessService baseProcessService = MultiConsumerBootstrap.reference(BaseProcessService.class);
        RpcContext.getContext().setAttachment("param","test");
        String result = (String) baseProcessService.process("测试功能实现控制!");
        for(int i=0;i<10;i++){
            RpcContext.getContext().setAttachment("param",i);
            baseProcessService.process("测试功能实现控制!");
//            log.info("execute the info data:{}",result);
        }
    }

}
