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
package com.hyts.assemble.uuidkey.hutool;


import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hyts.assemble.uuidkey.base.UUIDGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("hutool")
//@ConditionalOnExpression("'${uuidKey.type}'.equalsIgnoreCase('hutool')")
/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.extense.idgen.hutool
 * @author:LiBo/Alex
 * @create-date:2021-12-10 10:36 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class HutoolUUIDGenerator implements UUIDGenerator<String> {


    static cn.hutool.core.lang.generator.UUIDGenerator  generator =  new cn.hutool.core.lang.generator.UUIDGenerator();


    @Override
    public String nextId() {
        return generator.next();
    }


}
