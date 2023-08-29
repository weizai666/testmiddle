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
package com.hyts.assemble.sharding.model.mixed;

import com.hyts.assemble.sharding.enums.ShardingModelType;
import com.hyts.assemble.sharding.model.inline.DataSourceInlineRangeStrategy;
import com.hyts.assemble.sharding.model.inline.InlineModelStrategy;
import com.hyts.assemble.sharding.model.inline.TableInlineRangeStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;

/**
 * @project-name:standard-boot
 * @package-name:com.hyts.standard.sharding.model.mixed
 * @author:LiBo/Alex
 * @create-date:2021-08-20 15:17
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MixedModelStrategy extends InlineModelStrategy {

    /**
     * 分片策略模式:覆盖父类的
     */
    private ShardingModelType shardingModelType = ShardingModelType.MIXED;

    /**
     * 操作机制控制
     */
    private ShardingStrategyConfiguration shardingRuleConfiguration;



    public MixedModelStrategy(DataSourceInlineRangeStrategy dataSourceInlineRangeStrategy, TableInlineRangeStrategy tableInlineRangeStrategy, ShardingStrategyConfiguration shardingRuleConfiguration) {
        super(dataSourceInlineRangeStrategy, tableInlineRangeStrategy);
        this.shardingRuleConfiguration = shardingRuleConfiguration;
    }
}
