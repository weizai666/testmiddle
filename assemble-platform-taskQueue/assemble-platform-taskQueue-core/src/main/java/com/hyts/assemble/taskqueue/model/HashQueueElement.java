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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.config.redis
 * @author:LiBo/Alex
 * @create-date:2021-09-24 16:45
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@SuppressWarnings("serial")
//@Contended // 防止数据伪共享
@AllArgsConstructor
public class HashQueueElement<T> implements Serializable {

    /**
     * hash元数据标识ID
     */
    @NonNull
    private Long hashResourceId;

    /**
     * element元素对象模型
     */
    @Getter
    @NonNull
    private T element;


    public Long getHashResourceId() {
        return Optional.ofNullable(hashResourceId).orElse(ThreadLocalRandom.current().nextLong());
    }

}
