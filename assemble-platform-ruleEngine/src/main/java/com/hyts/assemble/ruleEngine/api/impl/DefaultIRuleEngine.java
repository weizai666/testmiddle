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
package com.hyts.assemble.ruleEngine.api.impl;

import com.hyts.assemble.ruleEngine.api.IRule;
import com.hyts.assemble.ruleEngine.api.IRuleEngine;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.ruleEngine.api.impl
 * @author:LiBo/Alex
 * @create-date:2022-08-11 00:36
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class DefaultIRuleEngine implements IRuleEngine {


    private AtomicReference<Rules> rulesAtomicReference  = new AtomicReference<>(new Rules());


    private AtomicReference<RulesEngine> rulesEngineAtomicReference  = new AtomicReference<>();


    /**
     * 初始化相关的规则引擎
     * @param ruleEngineName
     * @param rulesEngineParameters
     * @return
     */
    @Override
    public RulesEngine initRuleEngine(String ruleEngineName, RulesEngineParameters rulesEngineParameters) {
        rulesEngineAtomicReference.set(new DefaultRulesEngine(rulesEngineParameters));
        return rulesEngineAtomicReference.get();
    }

    /**
     * 操作参数控制
     * @param parameter
     * @return
     */
    @Override
    public RulesEngine execute(Map<String, Object> parameter) {
        Facts facts = new Facts();
        facts.put("paramCondition",parameter);
        rulesEngineAtomicReference.get().fire(rulesAtomicReference.get(),facts);
        return rulesEngineAtomicReference.get();
    }

    /**
     * 获取相关的规则信息对象
     * @return
     */
    @Override
    public Rules getRules() {
        return rulesAtomicReference.get();
    }

    /**
     * 添加规则
     * @param iRule
     * @return
     */
    @Override
    public IRuleEngine addRule(IRule... iRule) {
        Arrays.stream(iRule).forEach(param->{
            getRules().register(param);
        });
        return this;
    }

    /**
     * 添加规则集合
     * @param iRuleList
     * @return
     */
    @Override
    public IRuleEngine addRules(List<IRule> iRuleList) {
        iRuleList.stream().forEach(param->{
            getRules().register(param);
        });
        return this;
    }


    /**
     * 删除规则
     * @param iRule
     * @return
     */
    @Override
    public IRuleEngine removeRule(IRule... iRule) {
        Arrays.stream(iRule).forEach(param->{
            getRules().unregister(param);
        });
        return this;
    }

    /**
     * 删除规则
     * @param iRuleList
     * @return
     */
    @Override
    public IRuleEngine removeRules(List<IRule> iRuleList) {
        iRuleList.stream().forEach(param->{
            getRules().unregister(param);
        });
        return this;
    }


    @Override
    public IRuleEngine removeRules(String... ruleNames) {
        Arrays.stream(ruleNames).forEach(param->{
            getRules().unregister(param);
        });
        return this;
    }

    @Override
    public IRuleEngine removeRules(Set<String> ruleNames) {
        ruleNames.stream().forEach(param->{
            getRules().unregister(param);
        });
        return this;
    }

}
