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

import com.hyts.assemble.resource.drs.enums.DRSAllocateType;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @project-name:lhy-report
 * @package-name:com.lhy.report.resource
 * @author:LiBo/Alex
 * @create-date:2022-05-16 21:16
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 动态资源模型
 */
@SuppressWarnings("serial")
@Data
public class DRS<T> implements Serializable {

    /**
     * 唯一业务编码
     */
    private T identifyId;

    /**
     * 动态资源的名称
     */
    private String drsName;

    /**
     * 动态资源的类型
     */
    private DRSAllocateType drsType;

    /**
     * 权重计算的资源
     */
    private double weight = 1.0;

    /**
     * 资源占比
     */
    private double value;


    public DRS(T identifyId, String drsName, DRSAllocateType drsType, double weight, double value) {
        this.identifyId = identifyId;
        this.drsName = drsName;
        this.drsType = drsType;
        this.weight = weight;
        this.value = value;
    }

    public DRS(T identifyId, DRSAllocateType drsType, double value) {
        this.identifyId = identifyId;
        this.drsType = drsType;
        this.value = value;
    }

    public DRS(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DRS)) return false;
        DRS<?> drs = (DRS<?>) o;
        return getIdentifyId().equals(drs.getIdentifyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentifyId());
    }
}
