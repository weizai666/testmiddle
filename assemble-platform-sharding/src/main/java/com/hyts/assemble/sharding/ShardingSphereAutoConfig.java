package com.hyts.assemble.sharding;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author zj
 * @Date 2021/8/16 19:29
 */
@Configuration
public class ShardingSphereAutoConfig {

    @Bean
    public MonthTableShardingAlgorithm monthTableShardingAlgorithm(){
        MonthTableShardingAlgorithm monthTableShardingAlgorithm = new MonthTableShardingAlgorithm();
        return monthTableShardingAlgorithm;
    }
}
