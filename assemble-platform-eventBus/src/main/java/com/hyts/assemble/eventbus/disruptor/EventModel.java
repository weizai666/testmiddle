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
package com.hyts.assemble.eventbus.disruptor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @project-name:lhy-report
 * @package-name:com.lhy.lhy.report.event
 * @author:LiBo/Alex
 * @create-date:2021-09-25 00:32
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 事件(Event)就是通过 Disruptor 进行交换的数据类型。
 */
@NoArgsConstructor
@Data
@SuppressWarnings("all")
@ApiModel(value="事件驱动模型")
public class EventModel<T> implements Serializable {

    @ApiModelProperty(value="事件发布主题",name="事件发布主题")
    private String topic;

    @ApiModelProperty(value="事件对象模型",name="事件对象模型")
    private T entity;

}
