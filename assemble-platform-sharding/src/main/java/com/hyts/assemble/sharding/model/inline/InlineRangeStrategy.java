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
package com.hyts.assemble.sharding.model.inline;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @project-name:standard-boot
 * @package-name:com.hyts.standard.sharding.model
 * @author:LiBo/Alex
 * @create-date:2021-08-19 19:42
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: InLine模式策略机制
 */
@Accessors(chain = true)
@Data
public class InlineRangeStrategy {

    /**
     * 开始配置机制控制
     */
    private Integer startRangeNumber;

    /**
     * 结束范围机制控制
     */
    private Integer endRangeNumber;

    /**
     * 兼容复杂模式下的处理
     */
    private String shardingColumns;

    /**
     * 算法逻辑控制机制
     */
    private String algorithmExpression;

}
