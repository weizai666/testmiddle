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
package com.hyts.assemble.sharding.context;

import com.google.common.collect.Lists;
import com.hyts.assemble.sharding.anno.ShardingDataSource;
import com.hyts.assemble.sharding.anno.ShardingStrategy;
import com.hyts.assemble.sharding.anno.ShardingTable;
import com.hyts.assemble.sharding.enums.ShardingModelType;
import com.hyts.assemble.sharding.model.inline.DataSourceInlineRangeStrategy;
import com.hyts.assemble.sharding.model.inline.InlineModelStrategy;
import com.hyts.assemble.sharding.model.inline.TableInlineRangeStrategy;
import com.hyts.assemble.sharding.model.mixed.MixedModelStrategy;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @project-name:standard-boot
 * @package-name:com.hyts.standard.sharding.scan
 * @author:LiBo/Alex
 * @create-date:2021-08-20 17:59
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: bean对象注册信息表
 */
public class ShardingBeanDefintionRegistry {


    @ShardingStrategy(shardingTable = @ShardingTable(logicTableName = "",shardingColumn = "",startRangeNumber = 0,endRangeNumber = 0),
            shardingDataSource = @ShardingDataSource(logicDataSourceName="",startRangeNumber=0,endRangeNumber = 0,shardingColumn= ""))
    public List<Object> shardingDataModelList = Lists.newArrayList();


    /**
     * mixed数据信息列表
     */
    @Getter
    public static List<MixedModelStrategy> mixedModelStrategies = Lists.newArrayList();

    /**
     * inline数据信息列表
     */
    @Getter
    public static List<InlineModelStrategy> inlineModelStrategies = Lists.newArrayList();



    /**
     * 初始化对象模型注册器机制
     */
    public void initBeanDefinitionRegistry(){
        // 统计数据源
        if(CollectionUtils.isNotEmpty(shardingDataModelList)){
            shardingDataModelList.stream().forEach(param->{
                ShardingStrategy shardingStrategy = (ShardingStrategy) getAnnotationMetadataModel(param, ShardingStrategy.class);
                if(Objects.nonNull(shardingStrategy)){
                    InlineModelStrategy strategy = buildShardingStrategy(shardingStrategy);
                    if(shardingStrategy.shardingModelType() == ShardingModelType.MIXED){
                        mixedModelStrategies.add((MixedModelStrategy) strategy);
                    }else{
                        inlineModelStrategies.add(strategy);
                    }
                }
            });
        }
    }


    /**
     * 构建相关的分片策略机制
     * @return
     */
    private static InlineModelStrategy buildShardingStrategy(ShardingStrategy shardingStrategy ){
        //TODO 缺乏校验
        InlineModelStrategy modelStrategy = null;
        // 设置相关的mixed模式数据信息
        if(shardingStrategy.shardingModelType() == ShardingModelType.MIXED){
            // 暂时先以Mixed模式为主取做处理
            modelStrategy = new MixedModelStrategy();
        }else{
            modelStrategy = new InlineModelStrategy();
        }
        // 设置相关的数据源信息
        ShardingDataSource shardingDataSource = shardingStrategy.shardingDataSource();
        if(Objects.nonNull(shardingDataSource)){
            modelStrategy.setDataSourceInlineRangeStrategy((DataSourceInlineRangeStrategy)
                    new DataSourceInlineRangeStrategy(shardingDataSource.logicDataSourceName())
                            .setShardingColumns(shardingDataSource.shardingColumn())
                            .setStartRangeNumber(shardingDataSource.startRangeNumber())
                            .setEndRangeNumber(shardingDataSource.endRangeNumber()));
        }
        // 设置相关的数据表信息
        //TODO 缺乏校验
        ShardingTable shardingTable = shardingStrategy.shardingTable();
        if(Objects.nonNull(shardingTable)){
            modelStrategy.setTableInlineRangeStrategy((TableInlineRangeStrategy)
                    new TableInlineRangeStrategy(shardingTable.logicTableName())
                            .setShardingColumns(shardingTable.shardingColumn())
                            .setStartRangeNumber(shardingTable.startRangeNumber())
                            .setEndRangeNumber(shardingTable.endRangeNumber()));
        }
        return modelStrategy;
    }




    /**
     * 获取相关注解信息控制机制
     * @param object
     * @param annotationClassType
     * @return
     */
    public static Object getAnnotationMetadataModel(Object object,Class annotationClassType){
        Object annotationInstance = object.getClass().getAnnotation(annotationClassType);
        if(Objects.nonNull(annotationInstance) && annotationInstance instanceof ShardingStrategy) {
            ShardingStrategy shardingStrategy = (ShardingStrategy)annotationInstance;
            return shardingStrategy;
        }
        return null;
    }

}
