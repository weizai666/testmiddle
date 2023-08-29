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
package com.hyts.assemble.sharding.core;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @project-name:standard-boot
 * @package-name:com.hyts.standard.sharding.core
 * @author:LiBo/Alex
 * @create-date:2021-08-20 14:27
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 默认的sharding分片策略的配置器
 */
@Slf4j
public class MixedShardingStrategyConfiguration implements ShardingStrategyConfiguration {

    /**
     * 配置相关的分片策略机制
     * @return
     */
    private ShardingStrategyConfiguration getShardingStrategyConfiguration(String column){
        // 精确匹配
        PreciseShardingAlgorithm<Long> preciseShardingAlgorithm = (availableTargetNames, shardingValue) -> {
            String prefix = shardingValue.getLogicTableName(); //逻辑表名称
            Long columnField = shardingValue.getValue(); //编码
            long index = columnField % 2; //（分表路由索引）
            // t_tableName + "" + 0 = t_tableName_0
            String tableName = prefix + "_" +index;
            // 精确查询、更新之类的，可以返回不存在表，进而给前端抛出异常和警告。
            if (availableTargetNames.contains(tableName) == false) {
                log.error("PreciseSharding","columnField:{},not exisit the database or object {}！", columnField, tableName);
                return availableTargetNames.iterator().next();
            }
            return tableName;
//                return availableTargetNames.iterator().next();
        };
        // 范围匹配
        RangeShardingAlgorithm<Long> rangeShardingAlgorithm = (availableTargetNames, shardingValue) -> {
            String prefix = shardingValue.getLogicTableName();
            Collection<String> resList = new ArrayList<>();
            // 获取相关的数据值范围
            Range<Long> valueRange = shardingValue.getValueRange();
            // 如果没有上限或者下限的没有，则直接返回所有的数据表
            if (!valueRange.hasLowerBound() || !valueRange.hasUpperBound()) {
                return availableTargetNames;
            }
            // 获取下限数据范围
            long lower = shardingValue.getValueRange().lowerEndpoint();
            BoundType lowerBoundType = shardingValue.getValueRange().lowerBoundType();
            // 获取下限数据范围
            long upper = shardingValue.getValueRange().upperEndpoint();
            BoundType upperBoundType = shardingValue.getValueRange().upperBoundType();
            // 下限数据信息值
            long startValue = lower;
            long endValue = upper;
            // 是否属于开区间（下限）
            if (lowerBoundType.equals(BoundType.OPEN)) {
                startValue++; //缩减范围1
            }
            // 是否属于开区间（上限）
            if (upperBoundType.equals(BoundType.OPEN)) {
                endValue--; // 缩减范围1
            }
            // 进行计算相关所需要是实体表
            for (long i = startValue; i <= endValue ; i++) {
                long index = i % 2;
                String res = prefix + "_" +index;
                // 精确查询、更新之类的，可以返回不存在表，进而给前端抛出异常和警告。
                if (availableTargetNames.contains(res) == false) {
                    log.error("RangeSharding","orderId:{},not exisit the database or object{}！", i, res);
                }else{
                    resList.add(res);
                }
            }
            if (resList.size() == 0) {
                log.error("RangeSharding","not exisit the database or object，will be global table query ！column range:{}  to {}",startValue,endValue);
                return availableTargetNames;
            }
            return resList;
        };
        // 设置相关整体的算法整合
        ShardingStrategyConfiguration strategyConf = new StandardShardingStrategyConfiguration(column, preciseShardingAlgorithm, rangeShardingAlgorithm);
        return strategyConf;
    }




}
