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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.hyts.assemble.resource.drs.enums.DRSAllocateType;
import com.hyts.assemble.resource.drs.enums.DRSDataType;
import com.hyts.assemble.resource.drs.lock.DRSPoolResourceLocker;
import com.hyts.assemble.resource.drs.lock.LocalDRSPoolResourceLocker;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Optional;

/**
 * @project-name:lhy-report
 * @package-name:com.lhy.report.resource
 * @author:LiBo/Alex
 * @create-date:2022-05-16 21:36
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 动态资源调度池模型
 */
@Data
@Slf4j
@Accessors(chain = true)
public abstract class AbstractMemoryDRSPool<T> implements DRSPool<T>{

    /**
     * 资源池名称
     */
    private final String poolName;

    /**
     * 资源池元素类型
     */
    private DRSDataType drsDataType = DRSDataType.CPU_THREAD;


    /**
     * 资源池元素数量(最小值)
     */
    private volatile int minResourceNumber;

    /**
     * 资源池元素数量(最大值)
     */
    private volatile int maxResourceNumber;

    /**
     * 动态资源类型分配计划
     */
    private DRSAllocateType drsAllocateType;

    /**
     * 总体资源数据阈值
     */
    @Getter
    private Double resourceThresholdValue = DEFAULT_PERCENT_RESOURCE_VALUE;

    /**
     * 资源池元素容器
     */
    private final List<DRS<T>> drsCopyOnWriteArrayList = Lists.newCopyOnWriteArrayList();


    /**
     * 资源池元素锁控制:构造器相关发的锁控制器
     */
    private DRSPoolResourceLocker drsPoolResourceLocker;

    /**
     * 构造器
     */
    public AbstractMemoryDRSPool() {
        this(null,-1,-1,null,null);
    }

    /**
     * 构造器
     * @param poolName
     * @param minResourceNumber
     */
    public AbstractMemoryDRSPool(String poolName, int minResourceNumber) {
        this(null,minResourceNumber,-1,null,null);
    }

    /**
     * 构造器
     * @param minResourceNumber
     * @param maxResourceNumber
     */
    public AbstractMemoryDRSPool(int minResourceNumber, int maxResourceNumber) {
        this(null,minResourceNumber,maxResourceNumber,null,null);
    }

    /**
     * 构造器
     * @param poolName
     * @param minResourceNumber
     * @param maxResourceNumber
     */
    public AbstractMemoryDRSPool(String poolName, int minResourceNumber, int maxResourceNumber) {
        this(poolName,minResourceNumber,maxResourceNumber,null,null);
    }

    /**
     * 构造器
     * @param poolName
     * @param minResourceNumber
     * @param maxResourceNumber
     * @param drsAllocateType
     */
    public AbstractMemoryDRSPool(String poolName,
                                 int minResourceNumber,
                                 int maxResourceNumber,
                                 DRSAllocateType drsAllocateType) {
       this(poolName,minResourceNumber,maxResourceNumber,drsAllocateType,null);
    }

    /**
     * 构造器
     * @param poolName
     * @param minResourceNumber
     * @param maxResourceNumber
     * @param drsAllocateType
     * @param drsPoolResourceLocker
     */
    public AbstractMemoryDRSPool(String poolName, int minResourceNumber,
                                 int maxResourceNumber,
                                 DRSAllocateType drsAllocateType,
                                 DRSPoolResourceLocker drsPoolResourceLocker) {
        this.poolName = StringUtils.defaultIfEmpty(poolName,
                DEFAULT_POOL_NAME_PREFIX+SEQ_INCR.incrementAndGet());
        this.minResourceNumber = minResourceNumber<NumberUtils.BYTE_ZERO?
                NumberUtils.BYTE_ZERO:minResourceNumber;
        this.maxResourceNumber = maxResourceNumber<this.minResourceNumber?
                1<<10:maxResourceNumber;
        this.drsAllocateType = Optional.ofNullable(drsAllocateType).orElse(DRSAllocateType.AUTOMIC);
        this.drsPoolResourceLocker = Optional.ofNullable(drsPoolResourceLocker).
                orElseGet(LocalDRSPoolResourceLocker::new);
    }

    /**
     * 添加资源信息对象
     * @param drs
     * @return
     */
    @Override
    public boolean addResource(DRS<T> drs) {
        Preconditions.checkNotNull(drs,"the drs object model is not be null!");
        Preconditions.checkNotNull(drs.getDrsType(),"the drs object resource type is not be null!");
        Double percentValue = (drs.getWeight()*drs.getValue());
        Preconditions.checkArgument(percentValue
                 > NumberUtils.DOUBLE_ZERO,
                "compute allocate the resource is not be less or eq than zero");
        try{
            if(drsPoolResourceLocker.lock(drs)){
                Preconditions.checkArgument(drsCopyOnWriteArrayList.size() <= maxResourceNumber,
                        "over limit the maxResourceNumber");
                double requireResource = resourceThresholdValue * percentValue;
                double usedResource = currentUsedResourceValue();
                if(resourceThresholdValue - usedResource >= requireResource){
                    drsCopyOnWriteArrayList.add(drs);
                    log.info("require resource:{} is success!",requireResource);
                }else{
                    log.info("not get require resource:{} because the pool remain resource:{}",
                            requireResource,usedResource);
                    return Boolean.FALSE;
                }
            }
        }catch (Exception e){
            log.error("add request resource is faliure!",e);
            return Boolean.FALSE;
        }
        finally {
            drsPoolResourceLocker.unLock(drs);
        }
        return Boolean.TRUE;
    }

    /**
     * 获取资源信息
     * @param drs
     * @return
     */
    @Override
    public DRS<T> getResource(DRS<T> drs) {
        Preconditions.checkNotNull(drs,"the drs object model is not be null!");
        Preconditions.checkNotNull(drs.getIdentifyId(),"the drs object identify Id is not be null!");
        return drsCopyOnWriteArrayList.stream().filter(param->param.getIdentifyId()
                .equals(drs.getIdentifyId())).findAny().get();
    }

    /**
     * 数据资源值
     * @param drs
     * @return
     */
    @Override
    public double getResourceValue(DRS<T> drs) {
        DRS<T> drs1 = getResource(drs);
        return drs1.getValue()* resourceThresholdValue;
    }

    /**
     * 获取相关的数据集合信息
     * @return
     */
    @Override
    public List<DRS<T>> getResourceList() {
        return drsCopyOnWriteArrayList;
    }

    /**
     * 删除资源
     * @param drs
     * @return
     */
    @Override
    public boolean delResource(DRS<T> drs) {
        Preconditions.checkNotNull(drs,"the drs object model is not be null!");
        Preconditions.checkNotNull(drs.getIdentifyId(),"the drs object identify Id is not be null!");
        try{
            if (drsPoolResourceLocker.lock(drs)) {
                Preconditions.checkArgument(drsCopyOnWriteArrayList.size() > minResourceNumber,
                        "over limit the minResourceNumber");
                return drsCopyOnWriteArrayList.remove(drs);
            }else{
                return Boolean.FALSE;
            }
        }catch (Exception e){
            log.error("",e);
            return Boolean.FALSE;
        }finally {
            drsPoolResourceLocker.unLock(drs);
        }
    }

    /**
     * 当前资源池剩余的资源数据空间
     * @return
     */
    @Override
    public double currentUsedResourceValue() {
        return drsCopyOnWriteArrayList.stream().
                mapToDouble(param -> param.getValue() * param.getWeight()).sum() * resourceThresholdValue;
    }

    /**
     * 剩余资源池剩余的资源数据空间
     * @param weight
     * @return
     */
    @Override
    public double currentRemainResourceValue() {
        return resourceThresholdValue - currentUsedResourceValue();
    }

}

