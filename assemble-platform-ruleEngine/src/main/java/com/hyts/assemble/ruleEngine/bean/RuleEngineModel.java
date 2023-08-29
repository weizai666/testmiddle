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
package com.hyts.assemble.ruleEngine.bean;

import lombok.Data;
import org.jeasy.rules.core.RulesEngineParameters;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.ruleEngine.bean
 * @author:LiBo/Alex
 * @create-date:2022-08-11 00:49
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Data
public class RuleEngineModel {


    private String ruleEngineName;

    private RulesEngineParameters rulesEngineParameters;


    public RuleEngineModel(String ruleEngineName, RulesEngineParameters rulesEngineParameters) {
        this.ruleEngineName = ruleEngineName;
        this.rulesEngineParameters = rulesEngineParameters;
    }

    public RuleEngineModel(String ruleEngineName) {
        this.ruleEngineName = ruleEngineName;
        this.rulesEngineParameters = new RulesEngineParameters();
        rulesEngineParameters.setSkipOnFirstAppliedRule(Boolean.TRUE);
    }

    public static void main(String[] args) {

    }
}
