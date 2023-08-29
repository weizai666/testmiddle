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
package com.hyts.assemble.sharding.anno;

import com.hyts.assemble.sharding.core.MixedShardingStrategyConfiguration;
import com.hyts.assemble.sharding.enums.ShardingModelType;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @project-name:standard-boot
 * @package-name:com.hyts.standard.sharding.annotation
 * @author:LiBo/Alex
 * @create-date:2021-08-20 14:24
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 分片信息表
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
@Component
@Autowired(required = false)
@Qualifier("shardingStrategy")
public @interface ShardingStrategy {

    /**
     * 分片分表的模式机制
     * @return
     */
    ShardingModelType shardingModelType() default ShardingModelType.MIXED;

    /**
     * 分片表机制
     * @return
     */
    ShardingTable shardingTable();

    /**
     * 分片数据源机制
     * @return
     */
    ShardingDataSource shardingDataSource();

    /**
     * 只有在mix模式下才会有效果
     * @return
     */
    Class<? extends ShardingStrategyConfiguration> defaultShardingStrategyConfiguration() default
            MixedShardingStrategyConfiguration.class;

}
