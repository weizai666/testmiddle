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

import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.ruleEngine.api
 * @author:LiBo/Alex
 * @create-date:2022-08-11 00:35
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public interface IRuleEngine {

    /**
     * 初始化相关的RuleEngine
     * @param ruleEngineName
     * @param rulesEngineParameters
     * @return
     */
    RulesEngine initRuleEngine(String ruleEngineName,RulesEngineParameters rulesEngineParameters);

    /**
     * 执行规则处理方式
     * @param parameter
     * @return
     */
    RulesEngine execute( Map<String,Object> parameter);

    /**
     * 获取相关的rule对象服务机制
     * @return
     */
    Rules getRules();

    /**
     * 添加规则执行器
     * @param iRule
     * @return
     */
    IRuleEngine addRule(IRule... iRule);

    /**
     * 添加规则执行器
     * @param iRuleList
     * @return
     */
    IRuleEngine addRules(List<IRule> iRuleList);

    /**
     * 删除规则执行器
     * @param iRule
     * @return
     */
    IRuleEngine removeRule(IRule... iRule);

    /**
     * 删除规则执行器
     * @param iRuleList
     * @return
     */
    IRuleEngine removeRules(List<IRule> iRuleList);

    /**
     * 删除规则执行器
     * @param ruleNames
     * @return
     */
    IRuleEngine removeRules(String... ruleNames);

    /**
     * 删除规则执行器
     * @param ruleNames
     * @return
     */
    IRuleEngine removeRules(Set<String> ruleNames);

}
