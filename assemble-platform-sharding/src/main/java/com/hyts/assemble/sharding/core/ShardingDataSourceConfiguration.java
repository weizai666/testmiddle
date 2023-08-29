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

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hyts.assemble.sharding.context.ShardingBeanDefinitionListener;
import com.hyts.assemble.sharding.context.ShardingBeanDefintionRegistry;
import com.hyts.assemble.sharding.domain.ShardingDataSourceProperties;
import com.hyts.assemble.sharding.install.InlineShardingLogicTableInstaller;
import com.hyts.assemble.sharding.install.MixedShardingLogicTableInstaller;
import com.hyts.assemble.sharding.install.ShardingLogicTableIntaller;
import com.hyts.assemble.sharding.model.inline.DataSourceInlineRangeStrategy;
import com.hyts.assemble.sharding.model.inline.InlineModelStrategy;
import com.hyts.assemble.sharding.model.inline.TableInlineRangeStrategy;
import com.hyts.assemble.sharding.model.mixed.MixedModelStrategy;
import com.hyts.assemble.sharding.parser.InlineStringFormatModelParser;
import com.hyts.assemble.sharding.parser.wrapper.StrategyModelWrapperAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @project-name:standard-boot
 * @package-name:com.hyts.standard.sharding
 * @author:LiBo/Alex
 * @create-date:2021-08-19 19:19
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 分片数据源配置机制
 */

@Configuration
@Slf4j
public class ShardingDataSourceConfiguration {


    /**
     * 配置信息控制
     */
    @Bean
    @ConfigurationProperties(prefix = "sharding")
    public ShardingDataSourceProperties shardingDataSourceProperties(){
        return new ShardingDataSourceProperties();
    }


    /**
     * 数据源建立操作
     * @param applicationContext
     * @return
     */
    @Bean("shardingDataSource")
    public DataSource shardingDataSource(ApplicationContext applicationContext){
        // 构建shardingDataSource数据源配置对象
        // 分片规则表信息
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        // 广播表
//        shardingRuleConfiguration.setBroadcastTables(loadGlobalBroadCastTableList(applicationContext));
        // 绑定表
//        shardingRuleConfiguration.setBindingTableGroups(loadGlobalBindingTableList(applicationContext));

        ShardingBeanDefintionRegistry shardingBeanDefintionRegistry = applicationContext.getBean(ShardingBeanDefintionRegistry.class);
        shardingBeanDefintionRegistry.initBeanDefinitionRegistry();

        // 加载全局的分片规则默认配置服务 取第一个数据源为默认数据源
        loadGlobalShardingRuleDefaultConfig(shardingDataSourceProperties().getDataSource().get(0),applicationContext,shardingRuleConfiguration);
        // 获取系统中的相关数据信息数据源
        Map<String,DataSource> dataSourceMap = loadDataSources(applicationContext);
        // 安装全局系统服务的策略和逻辑表流程
        installFullLogicTableAndFullStrategyModel(applicationContext,shardingRuleConfiguration);
        try {
            DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfiguration,
                    getGlobalShardingProperties());
            return dataSource;
        } catch (SQLException throwables) {
            log.error("无法创建shardingJDBC数据源信息对象",throwables);
            throw new RuntimeException("无法创建数据源以及分片数据源信息对象",throwables);
        }
    }


    /**
     * 查询加载出所有相关的分区表(逻辑表)的列表信息
     * @return
     */
    public List<InlineModelStrategy> loadGlobalShardingLogicTableListForInline(ApplicationContext applicationContext){
        // 执行相关的
        if(CollectionUtil.isNotEmpty(ShardingBeanDefintionRegistry.getInlineModelStrategies())) {
            List dataSourceInlineRangeStrategys = Lists.newArrayList();
            ShardingBeanDefintionRegistry.getInlineModelStrategies().stream().forEach(param -> {
                TableInlineRangeStrategy tableInlineRangeStrategy = param.getTableInlineRangeStrategy();
                DataSourceInlineRangeStrategy dataSourceInlineRangeStrategy = param.getDataSourceInlineRangeStrategy();
                dataSourceInlineRangeStrategys.add(new InlineModelStrategy(dataSourceInlineRangeStrategy, tableInlineRangeStrategy));
            });
            return dataSourceInlineRangeStrategys;
        }
        return Lists.newArrayList();
    }


    /**
     * 获取相关的Mixed模式下的数据逻辑表列表信息
     * @param applicationContext
     * @return
     */
    public List<MixedModelStrategy> loadGlobalShardingLogicTableListForMixed(ApplicationContext applicationContext){
        // 执行相关的
        if(CollectionUtil.isNotEmpty(ShardingBeanDefintionRegistry.getMixedModelStrategies())) {
            List dataSourceInlineRangeStrategys = Lists.newArrayList();
            ShardingBeanDefintionRegistry.getMixedModelStrategies().stream().forEach(param -> {
                TableInlineRangeStrategy tableInlineRangeStrategy = param.getTableInlineRangeStrategy();
                DataSourceInlineRangeStrategy dataSourceInlineRangeStrategy = param.getDataSourceInlineRangeStrategy();
                MixedModelStrategy mixedModelStrategy = new MixedModelStrategy();
                mixedModelStrategy.setDataSourceInlineRangeStrategy(dataSourceInlineRangeStrategy);
                mixedModelStrategy.setTableInlineRangeStrategy(tableInlineRangeStrategy);
                dataSourceInlineRangeStrategys.add(mixedModelStrategy);
            });
            return dataSourceInlineRangeStrategys;
        }
        return Lists.newArrayList();
    }

    /**
     * 操作机制控制服务
     * @param applicationContext
     * @param shardingRuleConfiguration
     */
    public void installFullLogicTableAndFullStrategyModel(ApplicationContext applicationContext, ShardingRuleConfiguration shardingRuleConfiguration){
        //配置各个表的分库分表策略
        List<InlineModelStrategy> inlineModelStrategies = loadGlobalShardingLogicTableListForInline(applicationContext);
        // 控制配置规则机制控制服务能力
        InlineShardingLogicTableInstaller inlineShardingLogicTableInstaller = applicationContext.getBean(InlineShardingLogicTableInstaller.class);
        installTheLogicTableInlineStrategy(inlineShardingLogicTableInstaller,inlineModelStrategies,shardingRuleConfiguration);
        //TODO 暂时需要调整解决同时在多种模式下共同存在的情况
        /***************************开始处理mixed混合模式机制*****************************************/
        //配置各个表的分库分表策略
        List<MixedModelStrategy> mixedModelStrategies = loadGlobalShardingLogicTableListForMixed(applicationContext);
        MixedShardingLogicTableInstaller mixedShardingLogicTableInstaller = applicationContext.getBean(MixedShardingLogicTableInstaller.class);
        installTheLogicTableMixedStrategy(mixedShardingLogicTableInstaller,mixedModelStrategies,shardingRuleConfiguration);
    }



    /**
     * 处理机制,对所有相关的逻辑表进行策略的安装操作
     * @param inlineModelStrategies
     */
    public void installTheLogicTableInlineStrategy(InlineShardingLogicTableInstaller shardingLogicTableInstaller,
                                                   List<InlineModelStrategy> inlineModelStrategies, ShardingRuleConfiguration shardingRuleConfiguration){
                shardingLogicTableInstaller.install(inlineModelStrategies,shardingRuleConfiguration);
    }


    /**
     * 处理机制，对所有的相关的逻辑表进行策略安装（mixed模式）
     * @param shardingLogicTableInstaller
     * @param mixedModelStrategies
     * @param shardingRuleConfiguration
     */
    public void installTheLogicTableMixedStrategy(MixedShardingLogicTableInstaller shardingLogicTableInstaller,
                                                  List<MixedModelStrategy> mixedModelStrategies, ShardingRuleConfiguration shardingRuleConfiguration){
                shardingLogicTableInstaller.install(mixedModelStrategies,shardingRuleConfiguration);
    }

    /**
     * shardingRule配置控制类
     * @return
     */
    public ShardingRuleConfiguration loadGlobalShardingRuleDefaultConfig(String defaultDataSourceValue, ApplicationContext applicationContext,
                                                                         ShardingRuleConfiguration shardingRuleConfiguration){
        //配置默认数据源
        shardingRuleConfiguration.setDefaultDataSourceName(defaultDataSourceValue);
        // 设置默认的key值生成器操作机制
        shardingRuleConfiguration.setDefaultKeyGeneratorConfig(ShardingLogicTableIntaller.getKeyGeneratorConfigurationForDefault());
        // 分片规则机制控制配置
        return shardingRuleConfiguration;
    }


    /**
     * 获取加载全局的广播信息表集合
     * @return
     */
    public List<String> loadGlobalBroadCastTableList(ApplicationContext applicationContext){
        List<String> broadCastTables = Lists.newArrayList();
        //TODO 尚未实现
//        broadCastTables.add("tbl_config");
//        broadCastTables.add("tbl_config_type");
        return broadCastTables;
    }

    /**
     * 获取加载全局的绑定信息表集合
     * @return
     */
    public Set<String> loadGlobalBindingTableList(ApplicationContext applicationContext){
        Set<String> broadCastTables = Sets.newHashSet();
        //TODO 尚未实现
//        broadCastTables.add("tbl_config,tbl_config_type");
        return broadCastTables;
    }

    /**
     * 属性配置项，可以为以下属性
     * @return
     */
    public static Properties getGlobalShardingProperties(){
        // 配置对象
        Properties propertie = new Properties();
        //是否打印SQL解析和改写日志
        propertie.setProperty("sql.show",Boolean.TRUE.toString());
        //用于SQL执行的工作线程数量，为零则表示无限制
        propertie.setProperty("executor.size","4");
        //每个物理数据库为每次查询分配的最大连接数量
        propertie.setProperty("max.connections.size.per.query","1");
        //是否在启动时检查分表元数据一致性
        propertie.setProperty("check.table.metadata.enabled","false");
        // 覆盖操作机制控制
        return propertie;
    }


    /**
     * 初始化相关的数据源信息对象
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean("initOriginalDataSource")
    public DataSource initOriginalDataSource(DataSourceProperties dataSourceProperties){
        return DataSourceBuilder.create().driverClassName(dataSourceProperties.getDriverClassName())
                .password(dataSourceProperties.getPassword()).url(dataSourceProperties.getUrl()).username(dataSourceProperties.getUsername())
                .build();
    }

    /**
     * 创建数据源列表信息
     * @param applicationContext
     * @return
     */
    public Map<String,DataSource> loadDataSources(ApplicationContext applicationContext){
        //指定需要分库分表的数据源
        DataSource dataSource = applicationContext.getBean("initOriginalDataSource",DataSource.class);
        // 数据源操作机制控制
        Map<String,DataSource> dataSourceMap = Maps.newHashMap();
        shardingDataSourceProperties().getDataSource().stream().forEach(param->{
            dataSourceMap.put(param,dataSource);
        });
        return dataSourceMap;
    }

    /**
     * 事务管理器
     * @param dataSource
     * @return
     */
    @Bean
    @Autowired
    public PlatformTransactionManager shardingsphereTransactionManager(@Qualifier("shardingDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean
    public ShardingBeanDefinitionListener shardingBeanDefinitionListener(){
        return new ShardingBeanDefinitionListener();
    }


    @Bean()
    public ShardingBeanDefintionRegistry shardingBeanDefintionRegistry(){
        return new ShardingBeanDefintionRegistry();
    }


    @Bean
    public MixedShardingStrategyConfiguration mixedShardingStrategyConfiguration(){
        return new MixedShardingStrategyConfiguration();
    }


    @Bean
    public MixedShardingLogicTableInstaller mixedShardingLogicTableInstaller(){
        return new MixedShardingLogicTableInstaller();
    }


    @Bean
    public InlineShardingLogicTableInstaller inlineShardingLogicTableInstaller(){
        return new InlineShardingLogicTableInstaller();
    }


    @Bean
    public StrategyModelWrapperAdapter strategyModelWrapperAdapter(){
        return new StrategyModelWrapperAdapter();
    }



    @Bean()
    public InlineStringFormatModelParser inlineStringFormatModelParser(){
        return new InlineStringFormatModelParser();
    }


}
