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

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.engine.queue.constant
 * @author:LiBo/Alex
 * @create-date:2021-12-11 10:42 上午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public interface ExecuteTaskType {

    /**
     * 获取对应的ExecuteTaskType
     * @param code
     * @return
     */
    ExecuteTaskType getMeByTypeCode(byte code);


    String getName();

    /**
     * 类型编码
     * @return
     */
    byte getType();

    /**
     * 名称
     * @return
     */
    String getMessage();

    /**
     * 执行成功后的信息
     * @return
     */
    String getSuccessMessage();


    /**
     * 最小范围边界值
     * @return
     */
    Byte getMinRange();


    /**
     * 最大范围边界值
     * @return
     */
    Byte getMaxRange();
}
