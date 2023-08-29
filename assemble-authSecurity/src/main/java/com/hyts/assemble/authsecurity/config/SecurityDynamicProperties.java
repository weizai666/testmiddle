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
package com.hyts.assemble.authsecurity.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @project-name:call-center
 * @package-name:com.hyts.callcenter.config
 * @author:LiBo/Alex
 * @create-date:2022-01-20 13:35
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@ToString
@Component
public class SecurityDynamicProperties {

    @Getter
    @NacosValue(value = "${test}", autoRefreshed = true)
    private String test;

}
