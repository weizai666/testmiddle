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
package com.hyts.assemble.apigateway.security;


import cn.hutool.core.lang.Assert;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.apigateway.security
 * @author:LiBo/Alex
 * @create-date:2022-05-24 00:27
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Component
@Data
@EnableConfigurationProperties(AssembleSecurityProperties.class)
@ConfigurationProperties(prefix = "assemble.security")
public class AssembleSecurityProperties {

    /**
     * 访问auth-security服务的loadBalance地址，服务名
     */
    private String authUrl;

    /**
     * 不需要进行鉴权的接口和path。
     */
    private String[] ignorePath;

}
