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
package com.hyts.assemble.taskqueue.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.engine.queue.constant
 * @author:LiBo/Alex
 * @create-date:2021-12-11 10:40 上午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Getter
@AllArgsConstructor
public enum ExecuteAnalysisExpressionType {


    /**
     * 默认符合的值
     */
    DEFAULT_NOT_SUITABLE_VALUE("") {
        @Override
        public Object getValue() {
            return ThreadLocalRandom.current().nextLong(1000000);
        }
    },


    /**
     * 支持对当前时间戳的处理
     */
    CURRENT_TIME_PARAMETER("currentTime") {
        @Override
        public Object getValue() {
            return System.currentTimeMillis();
        }
    },

    /**
     * 支持对uuid的解析操作处理
     */
    DEFAULT_UUID_VALUE("uuid") {
        @Override
        public Object getValue() {
            return UUID.randomUUID().toString();
        }
    };


    private String propertyName;


    public abstract Object getValue();


    /**
     * 获取相关的值根据properties
     *
     * @param propertyName
     * @return
     */
    public static ExecuteAnalysisExpressionType getThisByPropertyName(String propertyName) {
        return Arrays.stream(ExecuteAnalysisExpressionType.values()).filter(param -> param.getPropertyName().equalsIgnoreCase(propertyName)).findAny().orElseGet(ExecuteAnalysisExpressionType::getDefaultNotSuitableValue);
    }

    /**
     * lazy执行
     *
     * @return
     */
    private static ExecuteAnalysisExpressionType getDefaultNotSuitableValue() {
        return ExecuteAnalysisExpressionType.DEFAULT_NOT_SUITABLE_VALUE;
    }
}


