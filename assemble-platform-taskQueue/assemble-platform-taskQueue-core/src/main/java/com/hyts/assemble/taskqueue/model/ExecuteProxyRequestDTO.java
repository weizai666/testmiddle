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
package com.hyts.assemble.taskqueue.model;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.domain.dto.execute
 * @author:LiBo/Alex
 * @create-date:2021-10-12 11:31
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExecuteProxyRequestDTO {

    private String executeClassName;

    private String executeMethodName;

    private Class executeTaskType;

    private String primaryKey;

    private Byte taskExecuteType;

    private JSONArray args;


}
