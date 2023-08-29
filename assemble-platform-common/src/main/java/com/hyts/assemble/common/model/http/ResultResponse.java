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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.base
 * @author:LiBo/Alex
 * @create-date:2021-11-05 9:46 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Data
@ApiModel(value="结果响应模型")
public class ResultResponse<T> implements Serializable {

    @ApiModelProperty(value="是否成功",name="是否成功:true/false")
    private boolean success;

    @ApiModelProperty(value="返回消息描述",name="返回消息描述")
    private String message;

    @ApiModelProperty(value="返回对象主题",name="返回对象主题")
    private T entity;

    /**
     * 结果
     * @param message
     * @param entity
     * @param resultFlag
     * @param <T>
     * @return
     */
    public static <T> ResultResponse result(String message, T entity, Boolean resultFlag){
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setSuccess(resultFlag);
        resultResponse.setEntity(entity);
        resultResponse.setMessage(message);
        return resultResponse;
    }

    /**
     * 成功
     * @param message
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> ResultResponse success(String message, T entity){
        return ResultResponse.result(message,entity,Boolean.TRUE);
    }

    /**
     * 成功
     * @return
     */
    public static ResultResponse success(String message){
        return ResultResponse.success(message,null);
    }

    /**
     * 成功
     * @return
     */
    public static ResultResponse success(){
        return ResultResponse.success(null,null);
    }

    /**
     * 成功
     * @return
     */
    public static <T> ResultResponse success(T entity){
        return ResultResponse.success("execute the process is success!",entity);
    }

    /**
     * 失败
     * @param message
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> ResultResponse failure(String message, T entity){
        return ResultResponse.result(message,entity,Boolean.FALSE);
    }

    /**
     * 失败
     * @return
     */
    public static ResultResponse failure(String message){
        return ResultResponse.failure(message,null);
    }

    /**
     * 失败
     * @return
     */
    public static <T> ResultResponse failure(T entity){
        return ResultResponse.failure("execute the process is failure!",entity);
    }

}
