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
package com.hyts.assemble.common.model.rpc;

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
public class RpcResponse<T> implements Serializable {

    private boolean success;

    private String message;

    private T entity;

    /**
     * 结果
     * @param message
     * @param entity
     * @param resultFlag
     * @param <T>
     * @return
     */
    public static <T> RpcResponse result(String message, T entity, Boolean resultFlag){
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setSuccess(resultFlag);
        rpcResponse.setEntity(entity);
        rpcResponse.setMessage(message);
        return rpcResponse;
    }

    /**
     * 成功
     * @param message
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> RpcResponse success(String message, T entity){
        return RpcResponse.result(message,entity,Boolean.TRUE);
    }

    /**
     * 成功
     * @return
     */
    public static RpcResponse success(String message){
        return RpcResponse.success(message,null);
    }

    /**
     * 成功
     * @return
     */
    public static RpcResponse success(){
        return RpcResponse.success(null,null);
    }

    /**
     * 成功
     * @return
     */
    public static <T> RpcResponse success(T entity){
        return RpcResponse.success("execute the process is success!",entity);
    }

    /**
     * 失败
     * @param message
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> RpcResponse failure(String message, T entity){
        return RpcResponse.result(message,entity,Boolean.FALSE);
    }

    /**
     * 失败
     * @return
     */
    public static RpcResponse failure(String message){
        return RpcResponse.failure(message,null);
    }

    /**
     * 失败
     * @return
     */
    public static <T> RpcResponse failure(T entity){
        return RpcResponse.failure("execute the process is failure!",entity);
    }

}
