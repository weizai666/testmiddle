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
package com.hyts.assemble.console.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * @project-name:assemble
 * @package-name:com.hyts.assemble.console.cache
 * @author:LiBo/Alex
 * @create-date:2022-06-21 19:55
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@EnableCaching
@Configuration
public class CacheConfiguration {


//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
//                        //可选设置序列化key new GenericJackson2JsonRedisSerializer()
//                        (new Jackson2JsonRedisSerializer(Object.class))
//                        //序列化value
//                        .serializeValuesWith(RedisSerializationContext.SerializationPair.
//                                fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class))))
//                .build();
//    }
}
