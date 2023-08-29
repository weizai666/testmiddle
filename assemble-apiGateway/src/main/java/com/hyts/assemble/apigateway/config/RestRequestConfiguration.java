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
package com.hyts.assemble.apigateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.gateway.config
 * @author:LiBo/Alex
 * @create-date:2022-01-11 15:14
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Configuration
public class RestRequestConfiguration {


    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


//    @LoadBalanced
//    @Bean
//    WebClient.Builder webClientBuilder() {
//        return WebClient.builder();
//    }
//
//    @Bean
//    WebClient webClient() {
//        return webClientBuilder().build();
//    }
}
