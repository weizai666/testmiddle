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

import com.hyts.assemble.ruleEngine.api.IRuleEngine;
import com.hyts.assemble.ruleEngine.api.impl.SampleRule;
import com.hyts.assemble.ruleEngine.bean.RuleEngineModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.ruleEngine
 * @author:LiBo/Alex
 * @create-date:2022-08-11 01:17
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class SampleEngine {



    public static void main(String[] args) {
        RuleEngineModel ruleEngineModel = new RuleEngineModel("test");
        IRuleEngine iRuleEngine = RuleEngineContext.createRuleEngine(ruleEngineModel);
        RuleEngineContext.bindRule(ruleEngineModel,new SampleRule());
        Map<String,Object> param = new HashMap<>();
        param.put("p1","test");
        iRuleEngine.execute(param);
    }
}
