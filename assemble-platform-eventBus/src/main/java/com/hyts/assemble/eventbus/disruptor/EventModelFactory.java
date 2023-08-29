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

import com.lmax.disruptor.EventFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @project-name:lhy-report
 * @package-name:com.lhy.lhy.report.event
 * @author:LiBo/Alex
 * @create-date:2021-09-25 00:33
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 定义事件工厂
 *
 * 事件工厂(Event Factory)定义了如何实例化前面第1步中定义的事件(Event)，需要实现接口 com.lmax.disruptor.EventFactory<T>。
 * Disruptor 通过 EventFactory 在 RingBuffer 中预创建 Event 的实例。
 * 一个 Event 实例实际上被用作一个“数据槽”，发布者发布前，先从 RingBuffer 获得一个 Event 的实例，
 * 然后往 Event 实例中填充数据，之后再发布到 RingBuffer 中，之后由 Consumer 获得该 Event 实例并从中读取数据。
 */
@Slf4j
public class EventModelFactory<T> implements EventFactory<EventModel<T>> {


    @Override
    public EventModel<T> newInstance() {
        return new EventModel<>();
    }

}
