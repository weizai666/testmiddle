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
package com.hyts.assemble.authsecurity.access;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @project-name:call-center
 * @package-name:com.hyts.callcenter.access
 * @author:LiBo/Alex
 * @create-date:2022-01-20 9:57
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@ToString
@Setter
@Getter
@ConfigurationProperties(prefix = "access-list")
@RefreshScope
@Configuration
@EnableConfigurationProperties(AccessListProperties.class)
public class AccessListProperties {

    /**
     * 白名单
     */
//    @Value(value="call-center.access-list.white-list")
//    @NacosValue(value="call-center.access-list.white-list",autoRefreshed = true)
    private volatile List<String> whiteList;

    /**
     * 黑名单
     */
  //  @Value(value="call-center.access-list.black-list")
//    @NacosValue(value="call-center.access-list.black-list",autoRefreshed = true)
    private volatile List<String> blackList;

}
