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
package com.hyts.assemble.resource.drs.pool;


import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @class-name:DRSPoolHandler
 * @author:Alex/Libo
 * @create-date:2022/5/16
 * @create-time:21:47
 * @copyright:github-hyts-config
 * @email:libo2dev@aliyun.com
 * @description:此类主要用于: 资源池的处理器机制
 */
public interface DRSPool<T> {


    AtomicInteger SEQ_INCR = new AtomicInteger(NumberUtils.BYTE_ZERO);



    String DEFAULT_POOL_NAME_PREFIX = "defaultDRSPool-";



    double DEFAULT_PERCENT_RESOURCE_VALUE = 1.0;

    /**
     * 添加资源
     * @param drs
     */
    boolean addResource(DRS<T> drs);

    /**
     * 获取相关的资源对象
     * @param drs
     * @return
     */
    DRS getResource(DRS<T> drs);

    /**
     *
     * @param drs
     * @return
     */
    double getResourceValue(DRS<T> drs);

    /**
     * 获取资源信息列表
     * @return
     */
    List<DRS<T>> getResourceList();

    /**
     * 删除资源
     * @return
     */
    boolean delResource(DRS<T> drs);

    /**
     * 获取当前使用资源数量
     * @return
     */
    double currentUsedResourceValue();

    /**
     * 获取当前仍然资源数量
     * @return
     */
    double currentRemainResourceValue();

}
