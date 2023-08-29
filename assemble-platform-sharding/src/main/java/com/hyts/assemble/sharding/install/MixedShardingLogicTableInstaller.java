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
package com.hyts.assemble.sharding.install;

import cn.hutool.core.collection.CollectionUtil;
import com.hyts.assemble.sharding.enums.ShardingModelType;
import com.hyts.assemble.sharding.model.inline.DataSourceInlineRangeStrategy;
import com.hyts.assemble.sharding.model.inline.TableInlineRangeStrategy;
import com.hyts.assemble.sharding.model.mixed.MixedModelStrategy;
import com.hyts.assemble.sharding.parser.StrategyModelParser;
import com.hyts.assemble.sharding.parser.wrapper.StrategyModelWrapperAdapter;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.NoneShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @project-name:standard-boot
 * @package-name:com.hyts.standard.sharding.install
 * @author:LiBo/Alex
 * @create-date:2021-08-20 12:50
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 混合模式逻辑表安装类
 */

@Service
public class MixedShardingLogicTableInstaller implements ShardingLogicTableIntaller<List<MixedModelStrategy>, ShardingRuleConfiguration, ShardingRuleConfiguration> {


    /**
     * 策略模式封装适配器
     */
    @Autowired
    StrategyModelWrapperAdapter strategyModelWrapperAdapter;


    /**
     * Sharding提供了5种分片策略：
     * StandardShardingStrategyConfiguration:标准分片策略, 提供对SQL语句中的=, IN和BETWEEN AND的分片操作支持
     * ComplexShardingStrategyConfiguration:复合分片策略, 提供对SQL语句中的=, IN和BETWEEN AND的分片操作支持。
     * InlineShardingStrategyConfiguration:Inline表达式分片策略, 使用Groovy的Inline表达式，提供对SQL语句中的=和IN的分片操作支持
     * HintShardingStrategyConfiguration:通过Hint而非SQL解析的方式分片的策略
     * NoneShardingStrategyConfiguration:不分片的策略
     * Sharding提供了以下4种算法接口：
     * PreciseShardingAlgorithm
     * RangeShardingAlgorithm
     * HintShardingAlgorithm
     * ComplexKeysShardingAlgorithm
     *
     * @return
     */
    static TableRuleConfiguration getLogicTableRuleConfiguration(MixedModelStrategy inlineModelStrategy, StrategyModelParser strategyModelParser) {
        // 操作机制处理功能实现
        DataSourceInlineRangeStrategy sourceInlineRangeStrategy = inlineModelStrategy.getDataSourceInlineRangeStrategy();
        // 表的内联机制控制策略
        TableInlineRangeStrategy tableInlineRangeStrategy = inlineModelStrategy.getTableInlineRangeStrategy();
        // 表规则配置服务机制控制
        TableRuleConfiguration result = new TableRuleConfiguration(tableInlineRangeStrategy.getLogicTableName(),
                (String) strategyModelParser.parser(inlineModelStrategy));
        /************************************新版本代替-旧版本代码*****************************************/
        //1、指定逻辑索引(oracle/PostgreSQL需要配置)
//        result.setLogicIndex("order_id");
        //2、指定逻辑表名
//        result.setLogicTable("t_order");
        //3、指定映射的实际表名
//        result.setActualDataNodes();
        //6、指定自增字段以及key的生成方式
//        result.setKeyGeneratorColumnName("order_id");
//        result.setKeyGenerator(new DefaultKeyGenerator());
        /************************************新版本代替-旧版本代码*****************************************/
        // 配置分库策略,缺省表示使用默认分库策略
        ShardingStrategyConfiguration dataSourceShardingSplit = Objects.isNull(sourceInlineRangeStrategy) ? new NoneShardingStrategyConfiguration() : inlineModelStrategy.getShardingRuleConfiguration();
        result.setTableShardingStrategyConfig(dataSourceShardingSplit);
        // inlineModelStrategy.getShardingRuleConfiguration() 暂时不完成分库操作机制：
        ShardingStrategyConfiguration tableShardingSplit = Objects.isNull(tableInlineRangeStrategy) ? new NoneShardingStrategyConfiguration() : inlineModelStrategy.getShardingRuleConfiguration();
        result.setTableShardingStrategyConfig(dataSourceShardingSplit);
        result.setDatabaseShardingStrategyConfig(tableShardingSplit);
        // 设置主外键创建算法值
        result.setKeyGeneratorConfig(ShardingLogicTableIntaller.
                getKeyGeneratorConfigurationForLogicTable(tableInlineRangeStrategy.getShardingColumns()));
        return result;
        //result.setDatabaseShardingStrategyConfig(new HintShardingStrategyConfiguration(new OrderDataBaseHintShardingAlgorithm()));
        //result.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("order_id,user_id",new orderComplexKeysShardingAlgorithm()));
        //result.setTableShardingStrategyConfig(new HintShardingStrategyConfiguration(new OrderTableHintShardingAlgorithm()));
    }



    /**
     * 安装相关的服务功能实现机制
     *
     * @param mixedModelStrategies
     * @param shardingRuleConfiguration
     * @return
     */
    @Override
    public ShardingRuleConfiguration install(List<MixedModelStrategy> mixedModelStrategies, ShardingRuleConfiguration shardingRuleConfiguration) {
        // 判断一下内联数据模型的数据安装集合信息
        if (CollectionUtil.isNotEmpty(mixedModelStrategies)) {
            Collection<TableRuleConfiguration> tableRuleConfigurations = shardingRuleConfiguration.getTableRuleConfigs();
            StrategyModelParser strategyModelParser = strategyModelWrapperAdapter.autoSwitchStrategyModelParser(ShardingModelType.MIXED);
            // 底层用的仍旧为内联解析器
            mixedModelStrategies.forEach(param -> {
                tableRuleConfigurations.add(getLogicTableRuleConfiguration(param, strategyModelParser));
            });
            return shardingRuleConfiguration;
        }
        return shardingRuleConfiguration;
    }


    @Override
    public ShardingRuleConfiguration unInstall(List<MixedModelStrategy> param, ShardingRuleConfiguration param2) {
        return null;
    }
}
