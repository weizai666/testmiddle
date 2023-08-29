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
package com.hyts.assemble.ruleEngine.api;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Priority;

import java.util.Map;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.ruleEngine.api
 * @author:LiBo/Alex
 * @create-date:2022-08-11 00:29
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public interface IRule{

    /**
     * 符合条件参数控制
     * @param paramCondition
     * @return
     */
    boolean condition(Map<String,Object> paramCondition);

    /**
     * 执行处理操作控制
     * @param paramCondition
     */
    void process(Map<String,Object> paramCondition);

    /**
     * 优先级操作处理
     * @return
     */
    int getPriority();
}
