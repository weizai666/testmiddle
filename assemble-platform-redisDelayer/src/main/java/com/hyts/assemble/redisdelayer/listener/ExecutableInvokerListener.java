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
package com.hyts.assemble.redisdelayer.listener;

/**
 * @project-name:wiz-shrding-framework
 * @package-name:com.wiz.sharding.framework.boot.starter.redisson.delayed
 * @author:LiBo/Alex
 * @create-date:2021-08-11 15:24
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@FunctionalInterface
public interface ExecutableInvokerListener<P,R>  {

    /**
     * 执行方法
     * @param param 返回值为以后callable使用
     * @return
     */
    R handle(P param);




}
