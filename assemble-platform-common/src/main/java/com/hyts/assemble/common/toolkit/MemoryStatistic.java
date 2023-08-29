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
package com.hyts.assemble.common.toolkit;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * @project-name:assemble
 * @package-name:com.hyts.assemble.common.toolkit
 * @author:LiBo/Alex
 * @create-date:2022-05-26 11:10
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
public class MemoryStatistic {


    /**
     * 统计内存处理控制
     */
    public static void statistic(){
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long xms = memoryBean.getHeapMemoryUsage().getInit();
        long xmx = memoryBean.getHeapMemoryUsage().getMax();//
//      long comm = memoryBean.getHeapMemoryUsage().getCommitted();
        long used = memoryBean.getHeapMemoryUsage().getUsed();
        log.info(xms / 1024 / 1024 + " " + xmx / 1024 / 1024 + " " + used / 1024 / 1024);
        Runtime rt = Runtime.getRuntime();
        long totalMemorySize = rt.totalMemory(); // 初始的总内存
        long maxMemorySiz = rt.maxMemory(); // 最大可用内存
        long freeMemorySize = rt.freeMemory(); // 当前可用内存
        log.info(totalMemorySize / 1024 / 1024 + " " + maxMemorySiz / 1024 / 1024 + " " + freeMemorySize / 1024 / 1024);
    }


    public static void main(String[] args){
        MemoryStatistic.statistic();
    }

}
