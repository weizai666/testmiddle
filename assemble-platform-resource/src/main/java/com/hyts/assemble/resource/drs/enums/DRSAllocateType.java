/**
 * Copyright [2020] [LiBo/Alex of copyright liboware@gmail.com ]
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
package com.hyts.assemble.resource.drs.enums;

/**
 * @project-name:lhy-report
 * @package-name:com.lhy.report.resource
 * @author:LiBo/Alex
 * @create-date:2022-05-16 21:50
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 动态数据资源配置类型
 */
public enum DRSAllocateType {

    /**
     * 哈希轮询分配
     */
    HASH,

    /**
     * 绝对资源分配
     */
    AVG,

    /**
     * 动态资源调度
     */
    DRS,

    /**
     * 随机资源调度
     */
    RANDOM,

    /**
     * 自动适配调度
     */
    AUTOMIC,

    ;
}
