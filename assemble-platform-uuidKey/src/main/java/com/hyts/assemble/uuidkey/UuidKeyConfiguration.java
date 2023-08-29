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
package com.hyts.assemble.uuidkey;

import cn.hutool.core.net.NetUtil;
import com.hyts.assemble.common.config.SwaggerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Slf4j
/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.uuidkey
 * @author:LiBo/Alex
 * @create-date:2022-05-24 22:51
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@ComponentScan("com.hyts.assemble.uuidkey")
@MapperScan("com.hyts.assemble.uuidkey.uidgen.mapper")
@Configuration
@EnableAutoConfiguration
public class UuidKeyConfiguration {


    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(UuidKeyConfiguration.class,args);
        ServerProperties serverProperties = applicationContext.getBean(ServerProperties.class);
        log.info("请访问swagger访问页面：{}", "http://"+ NetUtil.localIpv4s().stream().findFirst().get()+":"+
                serverProperties.getPort()+"/docs.html");
    }
}
