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
package com.hyts.assemble.distributeLock.base;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.extense.dlock
 * @author:LiBo/Alex
 * @create-date:2021-12-11 6:27 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public enum DistributeLockType {


    /**
     * 重入锁
     */
    REENTRANT_LOCK,

    /**
     * 非公平锁
     */
    FAIR_LOCK,

    /**
     * 联和锁
     */
    MULTI_LOCK,

    /**
     * 红锁
     */
    RED_LOCK,

    /**
     * 读写锁
     */
    READ_WRITE_LOCK,

    ;


}
