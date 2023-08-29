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
package com.hyts.assemble.ruleEngine;

import com.google.common.collect.Maps;
import com.hyts.assemble.ruleEngine.api.IRule;
import com.hyts.assemble.ruleEngine.api.IRuleEngine;
import com.hyts.assemble.ruleEngine.api.impl.DefaultIRuleEngine;
import com.hyts.assemble.ruleEngine.api.impl.SampleRule;
import com.hyts.assemble.ruleEngine.bean.RuleEngineModel;
import org.jeasy.rules.api.RulesEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.ruleEngine.api
 * @author:LiBo/Alex
 * @create-date:2022-08-11 00:41
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class RuleEngineContext {


    /**
     * 容器规则
     */
    private static final Map<String, IRuleEngine> rulesEngineMap = Maps.newConcurrentMap();


    /**
     * 创建规则引擎
     * @param ruleEngineModel
     * @param defaultEngine
     * @param className
     * @return
     */
    public static IRuleEngine createRuleEngine(RuleEngineModel ruleEngineModel, boolean defaultEngine, String className){
        if(defaultEngine){
            IRuleEngine iRuleEngine = new DefaultIRuleEngine();
            iRuleEngine.initRuleEngine(ruleEngineModel.getRuleEngineName(),
                    ruleEngineModel.getRulesEngineParameters());
            rulesEngineMap.putIfAbsent(ruleEngineModel.getRuleEngineName(),iRuleEngine);
            return iRuleEngine;
        }
        return null;
    }

    /**
     * 创建规则引擎
     * @param ruleEngineModel
     * @return
     */
    public static IRuleEngine createRuleEngine(RuleEngineModel ruleEngineModel){
        return createRuleEngine(ruleEngineModel,Boolean.TRUE,null);
    }

    /**
     * 绑定规则
     * @param ruleEngineModel
     * @param ruleObjects
     * @return
     */
    public static IRuleEngine bindRule(RuleEngineModel ruleEngineModel, IRule... ruleObjects){
        return rulesEngineMap.get(ruleEngineModel.getRuleEngineName()).addRule(ruleObjects);
    }


    /**
     * 解绑规则
     * @param ruleEngineModel
     * @param ruleObjects
     * @return
     */
    public static IRuleEngine unbindRule(RuleEngineModel ruleEngineModel, IRule... ruleObjects){
        return rulesEngineMap.get(ruleEngineModel.getRuleEngineName()).addRule(ruleObjects);
    }





}
