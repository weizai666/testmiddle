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
package com.hyts.assemble.redisdelayer.model;

import cn.hutool.core.lang.UUID;
import com.hyts.assemble.redisdelayer.listener.EventExecutableInvokerListener;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.Clock;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @project-name:wiz-shrding-framework
 * @package-name:com.wiz.sharding.framework.boot.starter.redisson.delayed
 * @author:LiBo/Alex
 * @create-date:2021-08-11 15:25
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 执行调用的事件模型
 */
@ToString
@Data
@ApiModel(value="延时队列调用模型")
public class ExecuteInvokerEvent<T> implements Serializable {

    /**
     * 业务主键编码
     */
    @ApiModelProperty(value="业务主键编码",name="业务主键编码",required = true)
    private String bizId;

    /**
     * 业务类型：可以是类名称
     */
    @ApiModelProperty(value="业务类型组名称",name="业务类型组名称",required = true,example = "DEFAULT_GROUP")
    private String bizGroup;

    /**
     * 创建事件事件-可以作为基准值
     */
    @ApiModelProperty(value="创建时间",name="创建时间",required = false,hidden = true)
    private Long createTime;

    /**
     * 延时时间
     */
    @ApiModelProperty(value="延时时间（秒数）",name="延时时间(秒数)",required = true,hidden = false)
    private Long delayedTime;

    /**
     * 延时时间戳
     */
    @ApiModelProperty(value="延时时间单位",name="延时时间单位",hidden = false)
    private TimeUnit timeUnit;

    /**
     * 预先计划执行事件操作 被动生产，最后会通过延时时间计算出来，此处用于备份冗余字段
     */
    @ApiModelProperty(value="预先计划执行事件操作",name="预先计划执行事件操作",hidden = true)
    private Date firedTime;

    /**
     * 数据执行模型体操作机制
     */
    @ApiModelProperty(value="数据执行模型体操作机制",name="数据执行模型体操作机制",hidden = true)
    private T dataModel;

    /**
     * 是否重试机制 次数 默认 0
     */
    @ApiModelProperty(value="是否重试机制",name="是否重试机制",hidden = true)
    private int retry;

    /**
     * 异步
     */
    @ApiModelProperty(value="异步",name="异步",hidden = true)
    private boolean async;


    public ExecuteInvokerEvent() {
    }

    /**
     * 检验操作机制 判断是否合法，TODO 未来可以覆盖更多
     * @param param
     * @return
     */
    boolean isNotNull(Object param){
        return Objects.nonNull(param);
    }



    /**
     * 检验操作机制 判断是否合法，TODO 未来可以覆盖更多
     * @param param
     * @return
     */
    boolean isValidate(ExecuteInvokerEvent<T> param){
        return isNotNull(param) ;
    }


    /**
     * 预先初始化机制操作
     * @param param
     * @return
     */
    public ExecuteInvokerEvent<T> preCondition(ExecuteInvokerEvent<T> param){
        //如果校验通过了
        if(isValidate(param)){
            param.setBizId(StringUtils.defaultIfBlank(param.getBizId(), UUID.fastUUID().toString()));
            long currentTime = Clock.system(ZoneId.systemDefault()).millis();
            param.setCreateTime(currentTime);
            //默认时间单位
            param.setTimeUnit(Optional.ofNullable(param.getTimeUnit()).orElse(EventExecutableInvokerListener.DEFAULT_DELAYED_TIMEUNIT));
            //默认时间偏移量
            param.setDelayedTime(Optional.ofNullable(param.getDelayedTime()).orElse(EventExecutableInvokerListener.DEFAULT_DELAYED_OSFFET));
            //计算相关的
            param.setAsync(EventExecutableInvokerListener.DEFAULT_IS_ASYNC_FLAG);
            // 重试次数
            param.setRetry(EventExecutableInvokerListener.DEFAULT_RETRY_NUM);
            // 执行组类型
            param.setBizGroup(StringUtils.isEmpty(param.getBizGroup())?EventExecutableInvokerListener.DEFAULT_BIZ_GROUP:param.getBizGroup());
            //计算触发时间值
            long triggerTime = currentTime+param.getTimeUnit().toMillis(param.getDelayedTime());
            //计算预先触发时间
            param.setFiredTime(new Date(triggerTime));
            return param;
        }
        throw new IllegalArgumentException("传输的参数出现异常，非法参数，请检查传参！");
    }

    /**
     * 构造器
     * @param bizId
     * @param bizGroup
     */
    public ExecuteInvokerEvent(String bizId, String bizGroup) {
        this.bizId = bizId;
        this.bizGroup = bizGroup;
    }

    /**
     * 构造器
     * @param bizGroup
     */
    public ExecuteInvokerEvent(String bizGroup) {
        this.bizGroup = bizGroup;
    }

}
