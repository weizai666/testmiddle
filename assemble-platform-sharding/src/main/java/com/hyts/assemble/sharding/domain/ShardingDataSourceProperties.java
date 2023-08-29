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
package com.hyts.assemble.sharding.domain;

import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * @project-name:standard-boot
 * @package-name:com.hyts.standard.sharding.domain
 * @author:LiBo/Alex
 * @create-date:2021-08-20 17:53
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@EnableConfigurationProperties(ShardingDataSourceProperties.class)
@Data
public class ShardingDataSourceProperties {


    List<String> dataSource;

}
