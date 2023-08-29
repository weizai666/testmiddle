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
import java.util.Comparator;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.engine.queue
 * @author:LiBo/Alex
 * @create-date:2021-12-11 11:32 上午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@AllArgsConstructor
@Getter
public enum DefaultExecuteTaskType implements ExecuteTaskType {

    /**
     *
     */
    SYSTEM_TASK_TYPE((byte)0,"system async queue execute success!","system async queue execute failure!"),

    /**
     *
     */
    APPLICATION_TASK_TYPE((byte)1,"application async queue execute success!","application async queue execute failure!"),


    PROMO_TASK_EXECUTE((byte)2,"promo number is sucess","promo number is failure"),


    DEFAULT_INSTANCE((byte)-1,"","");



    private byte code;


    private String successMessage;


    private String failureMessage;



    private static Byte minValue;

    private static Byte maxValue;


    static{
        minValue = Arrays.stream(DefaultExecuteTaskType.values()).filter(param->param.getType()>=0).min(Comparator.naturalOrder()).get().code;
        maxValue = Arrays.stream(DefaultExecuteTaskType.values()).filter(param->param.getType()>=0).max(Comparator.naturalOrder()).get().code;
    }

    /**
     * 不包含默认值
     * @return
     */
    @Override
    public Byte getMinRange() {
        return minValue;
    }


    /**
     * 不包含默认值
     * @return
     */
    @Override
    public Byte getMaxRange() {
        return maxValue;
    }



    @Override
    public ExecuteTaskType getMeByTypeCode(byte code) {
        return Arrays.stream(DefaultExecuteTaskType.values()).filter(param->param.code == code).findAny().orElse(DEFAULT_INSTANCE);
    }


    @Override
    public String getName() {
        return this.name();
    }


    @Override
    public byte getType() {
        return getCode();
    }


    @Override
    public String getMessage() {
        return getFailureMessage();
    }

    @Override
    public String getSuccessMessage() {
        return getSuccessMessage();
    }
}
