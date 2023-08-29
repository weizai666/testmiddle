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

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.engine.queue.constant
 * @author:LiBo/Alex
 * @create-date:2021-12-11 10:39 上午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Getter
@AllArgsConstructor
public enum ExecuteResultType {


//    0:未完成（待执行）
//    1:已完成
//    2:执行失败
//    3:暂停状态

    UNFINISHED((byte) 0,"NOT FINISHED"),

    FINISHED((byte) 1,"FINISHED"),

    FAILURE((byte) 2,"FAILURE"),

    PAUSE((byte) 3,"PAUSE"),

    ;

    private byte type;


    private String message;


    /**
     * 执行类根据编码操作
     * @param code
     * @return
     */
    public static ExecuteResultType getMeByTypeCode(byte code) {
        return Arrays.stream(values()).filter(param -> param.type == code).findAny().orElse(
                ExecuteResultType.UNFINISHED);
    }
}
