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
package com.hyts.assemble.common.model.http;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.base
 * @author:LiBo/Alex
 * @create-date:2021-11-05 9:38 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ResultRequest<T> implements Serializable {

    private String id;

    private T enity;

    public ResultRequest(T enity) {
        this.enity = enity;
    }

    public ResultRequest(String id, T enity) {
        this.id = id;
        this.enity = enity;
    }

    public ResultRequest(String id) {
        this.id = id;
    }
}
