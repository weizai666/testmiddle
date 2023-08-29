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
package com.hyts.assemble.minio.rpcapi;


import com.hyts.assemble.common.model.rpc.RpcRequest;
import com.hyts.assemble.common.model.rpc.RpcResponse;
import com.hyts.assemble.minio.model.OssProcessDTO;

/**
 * @project-name:dubbo-shopping
 * @package-name:com.dubbo.shopping.api.oss
 * @author:LiBo/Alex
 * @create-date:2022-04-17 17:26
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public interface RpcOssOperateRpcApi {

    /**
     * bucket
     * @param rpcRequest:内部存储对应的BucketName
     * @return
     */
    RpcResponse create(RpcRequest<String> rpcRequest);


    /**
     * 存储文件
     * @param rpcRequest filePath和bucketName
     * @return
     */
    RpcResponse upload(RpcRequest<OssProcessDTO> rpcRequest);

    /**
     * 删除文件
     * @param rpcRequest bucketName 和 fileName
     * @return
     */
    RpcResponse remove(RpcRequest<OssProcessDTO> rpcRequest);


    /**
     * 下载文件
     * @param rpcRequest bucketName 和 fileName
     * @return
     */
    RpcResponse download(RpcRequest<OssProcessDTO> rpcRequest);

}
