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
package com.hyts.assemble.dubbo3.comp.cache;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;

/**
 * @project-name:assemble
 * @package-name:com.hyts.assemble.dubbo3.comp.cache
 * @author:LiBo/Alex
 * @create-date:2022-06-09 22:57
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class RedisCache implements org.apache.dubbo.cache.Cache{


    private RedisClient redisClient;



    public RedisCache(){
        redisClient = RedisClient.create(RedisURI.create("localhost",6379));
    }


    @Override
    public void put(Object key, Object value) {
        StatefulRedisConnection redisConnection = redisClient.connect();
        redisConnection.sync().set(key+"",value+"");
        redisConnection.close();
    }

    @Override
    public Object get(Object key) {
        StatefulRedisConnection redisConnection = redisClient.connect();
        Object result = redisConnection.sync().get(key+"");
        redisConnection.close();
        return result;
    }
}
