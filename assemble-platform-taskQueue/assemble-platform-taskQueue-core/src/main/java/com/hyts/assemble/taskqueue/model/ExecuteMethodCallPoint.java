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

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.domain.dto.execute
 * @author:LiBo/Alex
 * @create-date:2021-09-27 13:29
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 当前接口调用点
 */
@NoArgsConstructor
@Data
@SuppressWarnings("serial")
public class ExecuteMethodCallPoint implements Serializable {

    /**
     * 反序列化调用方法类 = 全限定名
     */
    private String callSnapShotClassName;

    /**
     * 反序列化调用方法名称
     */
    private String callSnapShotMethodName;

    /**
     * 反序列化参数模型结构
     */
    private String callSnapShotParamType;

    /**
     * 反序列化参数模型结构
     */
    private String callSnapShotParamValue;

    /**
     * 构造器
     * @param callSnapShotClassName
     * @param callSnapShotMethodName
     * @param callSnapShotParamType
     * @param callSnapShotParamValue
     */
    public ExecuteMethodCallPoint(String callSnapShotClassName, String callSnapShotMethodName, String callSnapShotParamType, String callSnapShotParamValue) {
        this.callSnapShotClassName = callSnapShotClassName;
        this.callSnapShotMethodName = callSnapShotMethodName;
        this.callSnapShotParamType = callSnapShotParamType;
        this.callSnapShotParamValue = callSnapShotParamValue;
    }
}
