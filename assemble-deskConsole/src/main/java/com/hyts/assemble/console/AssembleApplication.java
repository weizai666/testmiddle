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
package com.hyts.assemble.console;

import cn.hutool.core.net.NetUtil;
import com.hyts.assemble.process.config.Knif4jConfiguration;
import com.hyts.assemble.eventbus.anno.EnableEventBus;
import com.hyts.assemble.minio.config.EnableMinioClient;
import com.hyts.assemble.ratelimiter.EnableRateLimiter;
import com.hyts.assemble.redisdelayer.anno.EnableDelayedQueue;
import com.hyts.assemble.taskqueue.anno.EnableTaskQueueProxy;
import com.hyts.assemble.uuidkey.config.EnableUUIDKey;
//import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.console
 * @author:LiBo/Alex
 * @create-date:2022-05-28 12:56
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@SpringBootApplication
@Import(Knif4jConfiguration.class)
@EnableRateLimiter
@EnableEventBus
@EnableUUIDKey
@EnableTaskQueueProxy
@EnableMinioClient
@EnableDelayedQueue
public class AssembleApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AssembleApplication.class,args);
        ServerProperties serverProperties = applicationContext.getBean(ServerProperties.class);
        log.info("请访问swagger访问页面：{}", "http://"+ NetUtil.localIpv4s().stream().findFirst().get()+":"+
                serverProperties.getPort()+"/doc.html");
    }

}
